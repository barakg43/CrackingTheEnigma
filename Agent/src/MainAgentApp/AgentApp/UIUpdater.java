package MainAgentApp.AgentApp;

import MainAgentApp.AgentApp.AgentStatus.AgentStatusController;
import MainAgentApp.AgentApp.CandidateStatus.CandidateStatusController;
import MainAgentApp.AgentApp.CandidateStatus.ProgressStatusRefresher;
import MainAgentApp.AgentApp.http.HttpClientAdapter;
import decryptionManager.DecryptionAgent;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

import static general.ConstantsHTTP.FAST_REFRESH_RATE;


public class UIUpdater {
    private final long SLEEP_TIME = 10;

    private Thread candidateListener=null;
    private static Task<Boolean> candidateListenerTask=null;


    private final SimpleBooleanProperty isGameEnded;
    private final SimpleBooleanProperty isDM_DoneAllTasks;
    private final SimpleBooleanProperty isCandidateUpdaterDone;


    private final String agentName;
    private final DecryptionAgent decryptionAgent;
    private final AgentStatusController agentProgressAndStatusController;
    private final CandidateStatusController candidateStatusController;





    private final Object candidateThreadPauseLock=new Object();
    private Boolean isCandidatePause;

    private final SimpleLongProperty queueTaskAmountProperty =new SimpleLongProperty(0);
    private final SimpleLongProperty completeTaskAmountProperty =new SimpleLongProperty(0);
    private final SimpleLongProperty pulledTaskAmountProperty =new SimpleLongProperty(0);
    private final SimpleLongProperty candidatesAmountProperty =new SimpleLongProperty(0);
    private TimerTask progressUpdaterTask;
    private Timer progressUpdaterTimer;

    public UIUpdater(String agentName,DecryptionAgent decryptionAgent, AgentStatusController agentProgressAndStatusController, CandidateStatusController candidateStatusController) {
        this.agentName = agentName;
        this.decryptionAgent = decryptionAgent;

        this.agentProgressAndStatusController = agentProgressAndStatusController;
        this.candidateStatusController = candidateStatusController;


        isDM_DoneAllTasks = new SimpleBooleanProperty();
        isCandidateUpdaterDone = new SimpleBooleanProperty();
        // taskDoneCounter.addPropertyChangeListener(newValue->counterProperty.set((Long)newValue));
        bindUpdaterToUIComponents();

//        ally1

        isGameEnded = new SimpleBooleanProperty(false);
        isGameEnded.addListener((observable, oldValue, newValue) -> {
            if (newValue)
                stopCandidateListener();
        });


    }
    public void setIsGameEndedValue(boolean isGameEndedValue)
    {
        isGameEnded.set(isGameEndedValue);
    }


    public void startCandidateListenerThread()
    {
        candidateListener.start();
    }
    private void createNewCandidateTask(){
        candidateListenerTask = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                System.out.println("Start Candidate Thread!");
                Supplier<TaskFinishDataDTO> supplier = decryptionAgent.getFinishQueueSupplier();
                TaskFinishDataDTO currentData;
                do {
                   // System.out.println(Thread.currentThread().getName()+ " before get Candidate! ");
                    currentData = supplier.get();
                  //  System.out.println("after get Candidate! ");
                    if (currentData != null)
                    {
                      //  System.out.println("new Candidate arrive!!");
                        sleepForAWhile(SLEEP_TIME);
                        candidateStatusController.addAllCandidate(currentData);
                        updateCandidatesAmount(currentData.getPossibleCandidates().size());
                        HttpClientAdapter.updateCandidate(currentData);
                    }
                    else
                        System.out.println("Finish Update all candidates!");


                } while (currentData != null);
                isCandidateUpdaterDone.set(true);
                return true;
            }
        };
    }
    public void startProgressStatusUpdater() {

        progressUpdaterTask = new ProgressStatusRefresher(queueTaskAmountProperty,
                completeTaskAmountProperty,
               pulledTaskAmountProperty,
                candidatesAmountProperty,
                agentName);

                progressUpdaterTimer = new Timer();
        progressUpdaterTimer.schedule(progressUpdaterTask, FAST_REFRESH_RATE, FAST_REFRESH_RATE);

    }

    public void stopProgressStatusUpdater() {
        if (progressUpdaterTask != null && progressUpdaterTimer != null) {
            progressUpdaterTask.cancel();
            progressUpdaterTimer.cancel();
        }
    }
    private void updateCandidatesAmount(int amount)
    {
        Platform.runLater(()->candidatesAmountProperty.set(candidatesAmountProperty.get()+amount));

    }
    public void updatePulledTaskAmount(long amount)
    {

        Platform.runLater(()->
                pulledTaskAmountProperty.set(pulledTaskAmountProperty.get()+amount));
    }
    private void bindUpdaterToUIComponents() {

        agentProgressAndStatusController.bindUIComponentToProperties(queueTaskAmountProperty,
                completeTaskAmountProperty,
                pulledTaskAmountProperty,
                candidatesAmountProperty);
        decryptionAgent.setCounterConsumers(this::setQueueTaskAmountValue,this::setTotalTaskComplete);
        // task message
        createNewCandidateTask();
        candidateListener=new Thread(candidateListenerTask,"Candidate Updater");

        resetAllUIData();
    }
    public void setQueueTaskAmountValue(long value)
    {
        Platform.runLater(()->queueTaskAmountProperty.set(value));
    }
    public void setTotalTaskComplete(long totalTaskComplete)
    {
        Platform.runLater(()->completeTaskAmountProperty.set(totalTaskComplete));
    }
    public static boolean isCandidateListenerAlive()
    {
        return candidateListenerTask!=null&& candidateListenerTask.isRunning();
    }
    public void resetAllUIData() {
        queueTaskAmountProperty.set(0);
        completeTaskAmountProperty.set(0);
        pulledTaskAmountProperty.set(0);
        candidatesAmountProperty.set(0);


    }

    public static void sleepForAWhile(long sleepTime) {
        if (sleepTime != 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {

            }
        }
    }


    public void stopCandidateListener()
    {
        Platform.runLater(()->{
            if(candidateListenerTask.isRunning())
            candidateListenerTask.cancel();
        });

    }






}

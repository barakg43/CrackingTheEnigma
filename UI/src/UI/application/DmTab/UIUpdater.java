package UI.application.DmTab;

import UI.application.DmTab.DMTaskComponents.CandidatesStatus.CandidatesStatusController;
import decryptionManager.DecryptionManager;
import decryptionManager.components.AtomicCounter;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;



public class UIUpdater {
    private final long SLEEP_TIME = 10;
    private Thread candidateListener=null;
    private static Task<Boolean> candidateListenerTask=null;
    private final DecryptionManager decryptionManager;
    private final CandidatesStatusController candidatesStatusController;
    private final ProgressDataDTO progressDataDTO;
    private final SimpleStringProperty messageProperty;
    private final SimpleDoubleProperty progressProperty;
    private  SimpleLongProperty counterProperty;
    private double totalTaskAmount= 0L;
    private final Consumer<String> messageManager;
    private final Consumer<Long> singleTaskTime;
    private final SimpleBooleanProperty isDoneBruteForce;
    private final SimpleBooleanProperty isDM_DoneAllTasks;
    private final SimpleBooleanProperty isCandidateUpdaterDone;
    private final AtomicCounter tasksDoneCounter;
private AtomicLong threadCounter;
    private Long totalTimeDuration;

    private final Object candidateThreadPauseLock=new Object();
    private Boolean isCandidatePause;

    public UIUpdater(DecryptionManager decryptionManager, ProgressDataDTO progressDataDTO, CandidatesStatusController candidatesStatusController) {
        this.decryptionManager = decryptionManager;
        this.candidatesStatusController = candidatesStatusController;
        this.progressDataDTO = progressDataDTO;
        messageProperty = new SimpleStringProperty();
        counterProperty = new SimpleLongProperty();
        progressProperty = new SimpleDoubleProperty(0);
        tasksDoneCounter = new AtomicCounter();
        isDoneBruteForce = new SimpleBooleanProperty();
        isDM_DoneAllTasks = new SimpleBooleanProperty();
        isCandidateUpdaterDone = new SimpleBooleanProperty();
        // taskDoneCounter.addPropertyChangeListener(newValue->counterProperty.set((Long)newValue));
        bindUpdaterToUIComponents();
        messageManager = this::updateMassage;
        singleTaskTime = this::getSingleTaskTime;
        decryptionManager.setCandidateListenerStarter(this::startCandidateListenerTread);
        threadCounter=new AtomicLong(0);
    }

    public SimpleBooleanProperty getIsDoneBruteForceProperty()
    {
        return isDoneBruteForce;
    }


    private void startCandidateListenerTread()
    {
        candidateListener.start();
    }
    private void createNewCandidateTask(){
        candidateListenerTask = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                System.out.println("Start Candidate Thread!");
                Supplier<TaskFinishDataDTO> supplier = decryptionManager.getFinishQueueSupplier();
                TaskFinishDataDTO currentData;
                do {

                    if (isCandidatePause) {
                        try {
                            synchronized (candidateThreadPauseLock) {
                                if (isCandidatePause) {
                                    System.out.println("Candidate Thread is paused");
                                    candidateThreadPauseLock.wait();
                                }
                                System.out.println("Candidate Thread resuming...");
                            }
                        } catch (InterruptedException ignored) {
                            return false;
                        }
                    }

                    currentData = supplier.get();
                    if (currentData != null)
                    {
                        sleepForAWhile(SLEEP_TIME);
                        candidatesStatusController.addAllCandidate(currentData);
                    }
                    else
                        System.out.println("Finish Update all candidates!");


                } while (currentData != null);
                isCandidateUpdaterDone.set(true);
                return true;
            }
        };
    }
    private void bindUpdaterToUIComponents() {
        // task message
        progressDataDTO.taskMessageProperty().bind(this.messageProperty);
        // task progress bar
        progressDataDTO.progressBarProperty().bind(this.progressProperty);
        progressDataDTO.totalNumberOfTasksProperty().set(String.valueOf(0));
         progressDataDTO.totalAmountTaskDoneProperty().bind(Bindings.format("%,d", counterProperty));
        // task percent label
        progressDataDTO.progressPercentProperty().bind( Bindings.concat(
                Bindings.format(
                        "%.0f",
                        Bindings.multiply(
                                this.progressProperty,
                                100)),
                " %"));

        isDoneBruteForce.addListener(((observable, oldValue, newValue) -> {
            if(newValue)
            {

                updateMassage("Done brute force and update all candidates!");

            }
        }));
        tasksDoneCounter.addPropertyChangeListener((counter) -> {
            Platform.runLater(()->counterProperty.set((Long) counter.getNewValue()));
            if(totalTaskAmount ==(Long) counter.getNewValue()) {
                isDM_DoneAllTasks.set(true);
                if(candidateListener.isAlive())
                     candidateListener.interrupt();
            }
        });


        decryptionManager.setTaskDoneAmount(tasksDoneCounter);
        resetAllUIData();


    }
    public static boolean isCandidateListenerAlive()
    {
        return candidateListenerTask!=null&& candidateListenerTask.isRunning();
    }
    private void resetAllUIData()

    {
        totalTimeDuration=0L;
        counterProperty.set(0);
        updateMassage("Waiting for start Brute Force!");
        isDM_DoneAllTasks.set(false);
        isCandidateUpdaterDone.set(false);
        isCandidatePause=Boolean.FALSE;
        progressDataDTO.getAverageTaskTimeProperty().set(String.valueOf(0));
        progressDataDTO.totalTimeTaskAmountProperty().set(String.valueOf(0));
        tasksDoneCounter.resetCounter();
        candidatesStatusController.clearAllTiles();
    }

    public static void sleepForAWhile(long sleepTime) {
        if (sleepTime != 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {

            }
        }
    }

    public void setupCandidateListener() {
        totalTaskAmount= decryptionManager.getTotalTasksAmount();
        progressDataDTO.totalNumberOfTasksProperty().set(String.format("%,.0f",totalTaskAmount));
        progressProperty.bind(Bindings.divide(counterProperty,totalTaskAmount));
        decryptionManager.setDataConsumer(messageManager,singleTaskTime);
        isDoneBruteForce.bind(Bindings.and(isDM_DoneAllTasks,isCandidateUpdaterDone));
        isDoneBruteForce.bind(Bindings.and(isDM_DoneAllTasks,isCandidateUpdaterDone));
        createNewCandidateTask();
        candidateListener=new Thread(candidateListenerTask,"Candidate Updater");
        resetAllUIData();

    }


    public void pauseCandidateListener(){
        isCandidatePause=true;
    }
    public void resumeCandidateListener(){
        isCandidatePause=false;
        synchronized (candidateThreadPauseLock){
        candidateThreadPauseLock.notifyAll();
        }
    }
    public void stopCandidateListener()
    {
        if(candidateListenerTask.isRunning())
            candidateListenerTask.cancel();
    }


    private String convertNanoTimeToTimerDisplay(long nanoTime)
    {

       return String.format("%02d:%02d:%02d.%04d",
                Duration.ofNanos(nanoTime).toHours()%24,
                Duration.ofNanos(nanoTime).toMinutes()%60,
                Duration.ofNanos(nanoTime).getSeconds()%60,
                Duration.ofNanos(nanoTime).toMillis()%10000
        );
    }
    public void getSingleTaskTime(long time) {
        totalTimeDuration+=time;
        if (tasksDoneCounter.getValue() > 0) {
            float millisSecTime=Duration.ofNanos(totalTimeDuration / tasksDoneCounter.getValue()).toMillis();
            sleepForAWhile(SLEEP_TIME);
            Platform.runLater(() -> {
            progressDataDTO.getAverageTaskTimeProperty()
                    .set(String.format("%,d Milliseconds",(int)millisSecTime));
            progressDataDTO.totalTimeTaskAmountProperty().
                        set(convertNanoTimeToTimerDisplay(totalTimeDuration));
            });
        }
    }
 public void updateMassage(String massage)
 {
  Platform.runLater(()-> messageProperty.set(massage));
 }

    public void resetData() {
        messageProperty.set("");
        counterProperty.set(0);
        tasksDoneCounter.resetCounter();
        progressDataDTO.totalNumberOfTasksProperty().set(String.valueOf(0));
        progressDataDTO.getAverageTaskTimeProperty().set(String.valueOf(0));
        progressDataDTO.totalTimeTaskAmountProperty().set(String.valueOf(0));
        candidatesStatusController.clearAllTiles();
    }



}

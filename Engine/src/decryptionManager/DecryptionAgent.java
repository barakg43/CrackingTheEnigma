package decryptionManager;

import agent.AgentDataDTO;
import agent.AgentSetupConfigurationDTO;
import decryptionManager.components.*;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import engineDTOs.MachineDataDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;

import java.io.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DecryptionAgent {


    private Dictionary dictionary;
    private final Engine engine;
    private final String alliesTeam;
    private final int threadAmount;
    private long tasksAmount;
    private final String agentName;
    private CodeCalculatorFactory codeCalculatorFactory;
//    private Consumer<Long> queueTaskConsumer;
//    private Consumer<Long> completeTaskConsumer;

    private BlockingQueue<TaskFinishDataDTO> successfulDecryption;
    private final AgentThreadPool threadsAgent;
    private byte[] engineCopyBytes;
    private AtomicCounter taskDoneAmount;
    private String cipheredString;
    private final BlockingQueue<Runnable> taskQueue;


    private Boolean isContestEnded;
    private Runnable startListener;

    private final Runnable notifyFinishTaskSession;
    public DecryptionAgent(AgentDataDTO agentDataDTO, Runnable notifyFinishTaskSession) {
        this.agentName = agentDataDTO.getAgentName();
        this.alliesTeam = agentDataDTO.getAllyTeamName();
        this.threadAmount = agentDataDTO.getThreadAmount();
        this.tasksAmount = agentDataDTO.getTasksSessionAmount();
//        threadsAgent =  Executors.newFixedThreadPool(threadAmount);
        this.notifyFinishTaskSession = notifyFinishTaskSession;
        engine=new EnigmaEngine();
        taskDoneAmount=new AtomicCounter();

        taskDoneAmount.addPropertyChangeListener((counter) -> {
            if((Long)counter.getNewValue() == tasksAmount) {
                doneRunningTaskSession();
            }
        });
        taskQueue=new LinkedBlockingDeque<>(agentDataDTO.getTasksSessionAmount());
        threadsAgent =new AgentThreadPool(threadAmount,threadAmount,20, TimeUnit.SECONDS,
                taskQueue,new AgentThreadFactory(),taskDoneAmount);

    }
    public Boolean getContestEndedFlag() {
        return isContestEnded;
    }
    private void doneRunningTaskSession()
    {
        taskDoneAmount.resetCounter();
        notifyFinishTaskSession.run();

    }


    public void setSetupConfiguration(AgentSetupConfigurationDTO agentSetupConfiguration)
    {

        engine.loadXMLFileFromStringContent(agentSetupConfiguration.getEngineXmlFile());
        engine.setCodeManually(agentSetupConfiguration.getCodeFormatDTO());
        MachineDataDTO machineData = engine.getMachineData();
        cipheredString=agentSetupConfiguration.getCipheredString();
        System.out.println("----------------------Contest cipheredString:"+cipheredString);
        dictionary=engine.getDictionary();
        saveEngineCopy();
        codeCalculatorFactory=new CodeCalculatorFactory(machineData.getAlphabetString(), machineData.getNumberOfRotorsInUse());
        successfulDecryption =new LinkedBlockingDeque<>();

    }

    public void setCandidateListenerStarter(Runnable startListener)
    {
        this.startListener=startListener;

    }

    public void setCounterConsumers(Consumer<Integer> queueTaskConsumer,Consumer<Long> incrementCompleteTaskConsumer) {

        taskDoneAmount.addPropertyChangeListener((counter) -> {

            long newVal=(Long)counter.getNewValue();
            long oldVal=(Long)counter.getOldValue();
            incrementCompleteTaskConsumer.accept(1L);
            queueTaskConsumer.accept(taskQueue.size());
            if(newVal == tasksAmount) {
                doneRunningTaskSession();
            }
        });
    }

    public void stop(){
        isContestEnded=true;
        threadsAgent.shutdownNow();
    }

    public Supplier<TaskFinishDataDTO> getFinishQueueSupplier()
    {
        return new TaskFinishSupplier(successfulDecryption,isContestEnded);
    }

    public void addTasksToAgent(DecryptedTask[] decryptedTasksArray)
    {
        tasksAmount =decryptedTasksArray.length;
        for(DecryptedTask task:decryptedTasksArray)
        {
            task.setupAgentConf(codeCalculatorFactory,
                                createNewEngineCopy(),
                                successfulDecryption,
                                dictionary,
                                taskDoneAmount,
                                agentName,
                                cipheredString);
            threadsAgent.submit(task);
        }
    }
    public void saveEngineCopy()
    {

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(  bos);
            os.writeObject(engine);
            engineCopyBytes= bos.toByteArray();
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private Engine createNewEngineCopy()  {
        ObjectInputStream oInputStream = null;
        Engine copyEngine=null;
        try {
            oInputStream = new ObjectInputStream(new ByteArrayInputStream(engineCopyBytes));
            copyEngine= (Engine) oInputStream.readObject();
            oInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return copyEngine;
    }

    static public void doneBruteForceTasks()
    {

     //   messageConsumer.accept("Finish running all tasks,finishing update all possible candidate.....");


    }


    public String getAlliesTeam() {
        return alliesTeam;
    }

    public int getThreadAmount() {
        return threadAmount;
    }

    public long getTasksAmount() {
        return tasksAmount;
    }




}

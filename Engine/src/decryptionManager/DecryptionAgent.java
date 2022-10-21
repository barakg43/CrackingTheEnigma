package decryptionManager;

import agent.AgentDataDTO;
import agent.AgentSetupConfigurationDTO;
import decryptionManager.components.*;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import engineDTOs.MachineDataDTO;
import enigmaEngine.Engine;
import enigmaEngine.EnigmaEngine;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
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
    private BlockingQueue<Runnable> taskQueue;
    private BlockingQueue<TaskFinishDataDTO> successfulDecryption;
    private final ExecutorService threadAgents;
    private byte[] engineCopyBytes;
    private AtomicCounter taskDoneAmount;
    private String cipheredString;

//    private Thread taskCreator;
    private double totalTaskAmount;
//    private static Consumer<String> messageConsumer;
//    public static Consumer<Long> currentTaskTimeConsumer;
    //    private long taskCounter;
    private static Boolean isFinishAllTask;
    public static volatile boolean isSystemPause;
    private boolean stopFlag;
    private Runnable startListener;
    public static final Object pauseLock=new Object();
    private final Runnable notifyFinishTaskSession;
    public DecryptionAgent(AgentDataDTO agentDataDTO, Runnable notifyFinishTaskSession) {
        this.agentName = agentDataDTO.getAgentName();
        this.alliesTeam = agentDataDTO.getAllyTeamName();
        this.threadAmount = agentDataDTO.getThreadAmount();
        this.tasksAmount = agentDataDTO.getTasksSessionAmount();
        threadAgents =  Executors.newFixedThreadPool(threadAmount);
        this.notifyFinishTaskSession = notifyFinishTaskSession;
        engine=new EnigmaEngine();
        taskDoneAmount=new AtomicCounter();

        taskDoneAmount.addPropertyChangeListener((counter) -> {
            if((Long)counter.getNewValue() == tasksAmount) {
                doneRunningTaskSession();
            }
        });
//        this.engine = engine;
//        dictionary=engine.getDictionary();
//        machineData=engine.getMachineData();
//        totalTaskAmount=0;

//        codeCalculatorFactory =new CodeCalculatorFactory(engine.getMachineData().getAlphabetString(),
//                machineData.getNumberOfRotorsInUse());

//        isFinishAllTask= Boolean.FALSE;

    }
    private void doneRunningTaskSession()
    {
        taskDoneAmount.resetCounter();
        notifyFinishTaskSession.run();

    }
    public void setTaskDoneAmount(AtomicCounter taskDoneAmount) {
        this.taskDoneAmount = taskDoneAmount;
    }

    public void setDataConsumer(Consumer<String> messageConsumer, Consumer<Long> currentTaskTimeConsumer) {
        //DecryptionManager.messageConsumer = messageConsumer;
       // DecryptionManager.currentTaskTimeConsumer =currentTaskTimeConsumer;
    }


    public void setSetupConfiguration(AgentSetupConfigurationDTO agentSetupConfiguration)
    {

        engine.loadXMLFileFromStringContent(agentSetupConfiguration.getEngineXmlFile());
        engine.setCodeManually(agentSetupConfiguration.getCodeFormatDTO());
        MachineDataDTO machineData = engine.getMachineData();
        cipheredString=agentSetupConfiguration.getCipheredString();
        dictionary=engine.getDictionary();
        saveEngineCopy();
        codeCalculatorFactory=new CodeCalculatorFactory(machineData.getAlphabetString(), machineData.getNumberOfRotorsInUse());
        successfulDecryption =new LinkedBlockingDeque<>();

    }

    public void setCandidateListenerStarter(Runnable startListener)
    {
        this.startListener=startListener;

    }



    public void stop(){
        isFinishAllTask=true;
        threadAgents.shutdownNow();
    }

    public Supplier<TaskFinishDataDTO> getFinishQueueSupplier()
    {
        return new TaskFinishSupplier(successfulDecryption,isFinishAllTask);
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
            threadAgents.submit(task);
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
        isFinishAllTask=true;

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

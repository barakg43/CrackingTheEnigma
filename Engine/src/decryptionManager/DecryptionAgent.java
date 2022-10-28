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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DecryptionAgent {


    private Dictionary dictionary;
    private final Engine engine;
    private final int threadAmount;
    private int taskSessionAmount;
    private final String agentName;
    private CodeCalculatorFactory codeCalculatorFactory;


    private BlockingQueue<TaskFinishDataDTO> successfulDecryption;
    private final ExecutorService threadsAgent;
    private byte[] engineCopyBytes;
    private AtomicCounter taskDoneAmount;
    private String cipheredString;

    private final AtomicInteger taskSessionCounter;
    private final AtomicLong totalTaskDoneCounter;

    private Boolean isContestEnded;
    private final Runnable notifyFinishTaskSession;
    private Consumer<Integer> queueTaskConsumer;
    private Consumer<Long> totalCompleteTaskConsumer;



    public DecryptionAgent(AgentDataDTO agentDataDTO, Runnable notifyFinishTaskSession) {
        this.agentName = agentDataDTO.getAgentName();
        this.threadAmount = agentDataDTO.getThreadAmount();
        this.taskSessionAmount = agentDataDTO.getTasksSessionAmount();
//        threadsAgent =  Executors.newFixedThreadPool(threadAmount);
        this.notifyFinishTaskSession = notifyFinishTaskSession;

        engine=new EnigmaEngine();





//            taskDoneAmount=new AtomicCounter();



        threadsAgent = Executors.newFixedThreadPool(threadAmount);

        taskSessionCounter = new AtomicInteger(0);
        totalTaskDoneCounter =new AtomicLong(0);
    }
    public Boolean getContestEndedFlag() {
        return isContestEnded;
    }
    private void doneRunningTaskSession()
    {
        taskSessionCounter.set(0);
        notifyFinishTaskSession.run();

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



    public void setCounterConsumers(Consumer<Integer> queueTaskConsumer,Consumer<Long> totalCompleteTaskConsumer) {

        this.queueTaskConsumer = queueTaskConsumer;
        this.totalCompleteTaskConsumer = totalCompleteTaskConsumer;
    }

    public void stop(){
        isContestEnded=true;
        threadsAgent.shutdownNow();
    }

    public Supplier<TaskFinishDataDTO> getFinishQueueSupplier()
    {
        return new TaskFinishSupplier(successfulDecryption);
    }

    public void addTasksToAgent(DecryptedTask[] decryptedTasksArray)
    {
        taskSessionAmount =decryptedTasksArray.length;
        for(DecryptedTask task:decryptedTasksArray)
        {
            task.setupAgentConf(codeCalculatorFactory,
                                createNewEngineCopy(),
                                successfulDecryption,
                                dictionary,
                                this::countersIncrement,
                                agentName,
                                cipheredString);
            threadsAgent.submit(task);
        }
    }
    public void countersIncrement()
    {

        queueTaskConsumer.accept(taskSessionAmount-taskSessionCounter.incrementAndGet());
        totalCompleteTaskConsumer.accept(totalTaskDoneCounter.incrementAndGet());
        if(taskSessionCounter.get() == taskSessionAmount) {
//            System.out.println("getting new session done:" + totalTaskDoneCounter.get());
            doneRunningTaskSession();
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







}

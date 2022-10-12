package decryptionManager;

import decryptionManager.components.*;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.BruteForceLevel;
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
    private final int threadNumber;
    private BruteForceLevel level=null;
    private long taskSize=0L;
    private String agentName;
    private CodeCalculatorFactory codeCalculatorFactory;
    private MachineDataDTO machineData;
    private BlockingQueue<Runnable> taskQueue;
    private BlockingQueue<TaskFinishDataDTO> successfulDecryption;
    private ExecutorService threadAgents;
    private byte[] engineCopyBytes;
    public AtomicCounter taskDoneAmount;

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
    private Runnable notifyFinishTaskSession;
    public DecryptionAgent(String agentName,String alliesTeam, int threadNumber, long taskSize, Runnable notifyFinishTaskSession) {
        this.alliesTeam = alliesTeam;
        this.threadNumber = threadNumber;
        this.taskSize = taskSize;
        threadAgents =  Executors.newFixedThreadPool(threadNumber);
        this.agentName = agentName;
        this.notifyFinishTaskSession = notifyFinishTaskSession;
        engine=new EnigmaEngine();
        taskDoneAmount=new AtomicCounter();

        taskDoneAmount.addPropertyChangeListener((counter) -> {
            if((Long)counter.getNewValue() ==taskSize) {
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
    public void doneRunningTaskSession()
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


    public void setSetupConfiguration(String engineXmlFile,CodeFormatDTO codeFormatDTO,String output)
    {
        engine.loadXMLFileFromStringContent(engineXmlFile);
        engine.setCodeManually(codeFormatDTO);
        machineData=engine.getMachineData();
        dictionary=engine.getDictionary();
        saveEngineCopy();
        codeCalculatorFactory=new CodeCalculatorFactory(machineData.getAlphabetString(),machineData.getNumberOfRotorsInUse());
        successfulDecryption =new LinkedBlockingDeque<>();

    }

    public void setCandidateListenerStarter(Runnable startListener)
    {
        this.startListener=startListener;

    }

    public void pause()  {
        isSystemPause =true;
//        try {
//            taskCreator.checkAccess();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
    public void resume()  {
        isSystemPause =false;
        synchronized (pauseLock)
        {
            //   System.out.println("Dm:Trying to resume");
            pauseLock.notifyAll();
        }
    }
    public void stop(){
        stopFlag=true;
        isFinishAllTask=true;
        threadAgents.shutdownNow();
    }

    public Supplier<TaskFinishDataDTO> getFinishQueueSupplier()
    {
        return new TaskFinishSupplier(successfulDecryption,isFinishAllTask);
    }

    public void addTasksToAgent(DecryptedTask[] decryptedTasksArray)
    {
        taskSize=decryptedTasksArray.length;
        for(DecryptedTask task:decryptedTasksArray)
        {
            task.setupAgentConf(codeCalculatorFactory,createNewEngineCopy(),successfulDecryption,dictionary,taskDoneAmount,agentName);
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

    public int getThreadNumber() {
        return threadNumber;
    }

    public long getTaskSize() {
        return taskSize;
    }
}

package decryptionManager;

import decryptionManager.components.*;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.BruteForceLevel;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import engineDTOs.MachineDataDTO;
import engineDTOs.RotorInfoDTO;
import enigmaEngine.Engine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class DecryptionManager {


    private final Dictionary dictionary;
    private final Engine engine;
    private BruteForceLevel level=null;
    private int taskSize=0;
    private final CodeCalculatorFactory codeCalculatorFactory;
    private final MachineDataDTO machineData;
    private BlockingQueue<Runnable> taskQueue;
    private BlockingQueue<TaskFinishDataDTO> successfulDecryption;
     private AgentsThreadPool agents;
    private byte[] engineCopyBytes;
    private final int QUEUE_SIZE=1000;
    public  AtomicCounter taskDoneAmount;
    private String output;
    private Thread taskCreator;
    private double totalTaskAmount;
    private static Consumer<String> messageConsumer;
    public static Consumer<Long> currentTaskTimeConsumer;
//    private long taskCounter;
    private static Boolean isFinishAllTask;
    public static volatile boolean isSystemPause;
    private boolean stopFlag;
    private Runnable startListener;
    public static final Object pauseLock=new Object();
    public DecryptionManager(Engine engine) {


        this.engine = engine;
        dictionary=engine.getDictionary();
        machineData=engine.getMachineData();
        totalTaskAmount=0;

        codeCalculatorFactory =new CodeCalculatorFactory(engine.getMachineData().getAlphabetString(),
                machineData.getNumberOfRotorsInUse());

        isFinishAllTask= Boolean.FALSE;

    }

    public void setTaskDoneAmount(AtomicCounter taskDoneAmount) {
        this.taskDoneAmount = taskDoneAmount;
    }

    public void setDataConsumer(Consumer<String> messageConsumer, Consumer<Long> currentTaskTimeConsumer) {
        DecryptionManager.messageConsumer = messageConsumer;
        DecryptionManager.currentTaskTimeConsumer =currentTaskTimeConsumer;
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
    public void setSetupConfiguration(BruteForceLevel level,int agentAmount,int taskSize)
    {
    this.level=level;
        this.taskSize=taskSize;
    totalTaskAmount=0;
    calculateTotalTaskAmount(level);
    if(taskSize<1)
        throw new RuntimeException("task size must be positive number!(greater then 0)");
    if(agentAmount<2||agentAmount>50)
        throw new RuntimeException("Agent amount must be between 2 and 50");
    if(level==null)
        throw new RuntimeException("Brute force level must be selected");
    successfulDecryption =new LinkedBlockingDeque<>();
    taskQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
    agents=new AgentsThreadPool(agentAmount,agentAmount,20, TimeUnit.SECONDS,
            taskQueue,new AgentThreadFactory(),taskDoneAmount);
    }
    public double getTotalTasksAmount() {
        if(totalTaskAmount==0)
            calculateTotalTaskAmount(level);
        return totalTaskAmount;
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
        agents.shutdownNow();
    }

    public Supplier<TaskFinishDataDTO> getFinishQueueSupplier()
    {
        return new TaskFinishSupplier(successfulDecryption,isFinishAllTask);
    }
    public void startBruteForce(String output)
    {
        this.output=output;
        agents.prestartAllCoreThreads();
        totalTaskAmount=0;
        isFinishAllTask=false;
        isSystemPause =false;
        stopFlag=false;
        startListener.run();
        taskCreator=new Thread(()-> {
            agents.setTotalTaskAmount(getTotalTasksAmount());
                try {
                    CodeFormatDTO startingCode = engine.getCodeFormat(false);
                    switch (level) {
                        case EASY:
                            messageConsumer.accept("Starting brute force easy level");
                            createTaskEasyLevel(startingCode);
                            break;
                        case MIDDLE:
                            messageConsumer.accept("Starting brute force middle level");
                            createTaskMiddleLevel(startingCode);
                            break;
                        case HARD:
                            messageConsumer.accept("Starting brute force hard level");
                            createTaskHardLevel(startingCode);
                            break;
                        case INSANE:
                            messageConsumer.accept("Starting brute force impossible level");
                            createTaskImpossibleLevel();
                            break;
                    }

                } catch (RuntimeException e) {
                    throw new RuntimeException("Error when creating tasks: "+e);
                }
        },"Task Creator DM Thread");
        taskCreator.start();

    }

    static public void doneBruteForceTasks()
    {

        messageConsumer.accept("Finish running all tasks,finishing update all possible candidate.....");
        isFinishAllTask=true;

    }



    private void createTaskImpossibleLevel() {

        int rotorNumberInSystem=machineData.getNumberOfRotorInSystem();
        int rotorNumberInUsed=machineData.getNumberOfRotorsInUse();

        int[] rotorIdSet = new int[rotorNumberInUsed];
        // initialize with the lowest lexicographic rotorIdSet
        for (int i = 0; i < rotorNumberInUsed; i++) {
            rotorIdSet[i] = i;
        }
        while (rotorIdSet[rotorNumberInUsed - 1] < rotorNumberInSystem) {
            if(stopFlag)
                return;

            RotorInfoDTO[] rotorInfoDTO=new RotorInfoDTO[rotorNumberInUsed];
            for (int i = 0; i <rotorNumberInUsed ; i++) {
                rotorInfoDTO[i]=new RotorInfoDTO(rotorIdSet[i]+1,0,
                                        codeCalculatorFactory.getFirstLetter());
            }

            CodeFormatDTO codeFormatDTO=new CodeFormatDTO(rotorInfoDTO,
                    machineData.getReflectorIdList().get(0),new ArrayList<>());
            createTaskHardLevel(codeFormatDTO);

            // generate next rotorIdSet in lexicographic order
            int temp = rotorNumberInUsed - 1;
            while (temp != 0 && rotorIdSet[temp] == rotorNumberInSystem - rotorNumberInUsed + temp) {
                temp--;
            }
            rotorIdSet[temp]++;
            for (int i = temp + 1; i < rotorNumberInUsed; i++) {
                rotorIdSet[i] = rotorIdSet[i - 1] + 1;
            }
        }
    }


    private void createTaskHardLevel(CodeFormatDTO codeFormatDTO)
    {
        CodeFormatDTO currentCode=codeFormatDTO;
        int rotorUsedNumber=machineData.getNumberOfRotorsInUse();
        Permuter permuteFactory=new Permuter(rotorUsedNumber);
        int[] rotorId=new int[ rotorUsedNumber];
        for (int i = 0; i <rotorUsedNumber ; i++) {
            rotorId[i]=codeFormatDTO.getRotorInfoArray()[i].getId();
        }
        int[] currentPermutationIndex=permuteFactory.getNext();

        while (currentPermutationIndex!=null)
        {
            if(stopFlag)
                return;

            RotorInfoDTO[] currentRotorInfo=codeFormatDTO.getRotorInfoArray();

            for (int i = 0; i < rotorUsedNumber; i++) {
                int indexPermute=currentPermutationIndex[i];
                int rotorID=rotorId[indexPermute];
                currentRotorInfo[i]=new RotorInfoDTO(rotorID,
                        currentRotorInfo[indexPermute].getDistanceToWindow(),
                        currentRotorInfo[indexPermute].getStatingLetter());

            }

            currentCode=new CodeFormatDTO(currentRotorInfo,codeFormatDTO.getReflectorID(), new ArrayList<>());
            createTaskMiddleLevel(currentCode);
            currentPermutationIndex=permuteFactory.getNext();
        }


    }

    private void createTaskMiddleLevel(CodeFormatDTO codeFormatDTO){
        List<String> reflectorIdList = machineData.getReflectorIdList();
        CodeFormatDTO currentCode;
        for(String reflector:reflectorIdList)
        {
            if(stopFlag)
                return;
           // System.out.println("reflector " + reflector);
            currentCode=new CodeFormatDTO(codeFormatDTO.getRotorInfoArray(),reflector,codeFormatDTO.getPlugboardPairDTOList());
          //  System.out.println("After new reflector: " + currentCode);
            createTaskEasyLevel(currentCode);

        }


    }

    private void createTaskEasyLevel(CodeFormatDTO codeFormatDTO)
    {
        CodeFormatDTO currentCode=null,temp;

        temp=resetAllPositionToFirstPosition(codeFormatDTO);
        double i = 0;

        while(temp!=null&&!stopFlag){
            currentCode=temp;
            try {
                if(isSystemPause) {
                    synchronized (pauseLock) {
                        pauseLock.wait();
                    }
                }

               // Thread.sleep(5000);//TODO: thread pool delayed
              //  System.out.println("Task creator is running!");

                taskQueue.put(new DecryptedTask(CodeFormatDTO.copyOf(currentCode),
                output,
                  codeCalculatorFactory,
                 createNewEngineCopy(),
             taskSize,
            successfulDecryption,
            dictionary));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            temp=codeCalculatorFactory.getNextCodeIndexOffset(temp,taskSize);
            //System.out.println("current code  " + currentCode);
        }


    }

    private CodeFormatDTO resetAllPositionToFirstPosition(CodeFormatDTO codeFormatDTO) {
        RotorInfoDTO[] rotorInfoArray = codeFormatDTO.getRotorInfoArray();
        for (int i = 0; i < rotorInfoArray.length; i++) {
            rotorInfoArray[i] = new RotorInfoDTO(rotorInfoArray[i].getId(), 0,
                    codeCalculatorFactory.getFirstLetter());
        }
        return new CodeFormatDTO(rotorInfoArray, codeFormatDTO.getReflectorID(), new ArrayList<>());


    }


    private double calculateEasyLevelTaskAmount()
    {
        double totalAmount=codeCalculatorFactory.getCodeConfAmount()/taskSize;
        if(codeCalculatorFactory.getCodeConfAmount()%taskSize>0)
            totalAmount+=1;
        return Math.floor(totalAmount);
    }
    private double calculateMiddleLevelTaskAmount()
    {
        return (machineData.getReflectorIdList().size())*calculateEasyLevelTaskAmount();
    }
    private double calculateHardLevelTaskAmount()
    {
        double totalAmount=calculateFactorial(machineData.getNumberOfRotorsInUse());
        return totalAmount*calculateMiddleLevelTaskAmount();
    }
    private double calculateImpossibleLevelTaskAmount()
    {
        int n=machineData.getNumberOfRotorInSystem();
        int k=machineData.getNumberOfRotorsInUse();
        double totalAmount=calculateFactorial(n)
                /(calculateFactorial(k)*calculateFactorial(n-k));
        return totalAmount*calculateHardLevelTaskAmount();
    }
    private double calculateFactorial(int number) {
        double fact = 1;
        for (int i = 1; i <= number; i++) {
            fact = fact * i;
        }
        return fact;
    }
    private void calculateTotalTaskAmount(BruteForceLevel level)
    {
            switch (level)
            {
                case EASY:
                    totalTaskAmount= calculateEasyLevelTaskAmount();
                break;
                case MIDDLE:
                    totalTaskAmount= calculateMiddleLevelTaskAmount();
                    break;
                case HARD:
                    totalTaskAmount= calculateHardLevelTaskAmount();
                    break;
                case INSANE:
                    totalTaskAmount= calculateImpossibleLevelTaskAmount();
                    break;
            }

    }

}




package decryptionManager;

import decryptionManager.components.*;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.BruteForceLevel;
import engineDTOs.MachineDataDTO;
import engineDTOs.RotorInfoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;


public class DecryptionManager {


    private final CodeFormatDTO startingCode;

    private final MachineDataDTO machineData;
    // private final Engine engine;
    private BruteForceLevel level=null;
    private int taskSize=0;
    private final CodeCalculatorFactory codeCalculatorFactory;
    private BlockingQueue<DecryptedTask> taskQueue;
    private final int QUEUE_SIZE=1000;
    public  AtomicCounter taskDoneAmount;
    private String output;
    private Thread taskCreator;
    private double totalTaskAmount;
//    private static Consumer<String> messageConsumer;
   // public static Consumer<Long> currentTaskTimeConsumer;
//    private long taskCounter;
    private static Boolean isFinishAllTask;
    public static volatile boolean isSystemPause;
    private boolean stopFlag;
    private Runnable startListener;
    public static final Object pauseLock=new Object();
    private Consumer<String> messageConsumer;

    public DecryptionManager(CodeFormatDTO initialCode, MachineDataDTO machineDataDTO) {

    this.machineData =machineDataDTO;
       // this.engine = engine;
         startingCode =initialCode;

        totalTaskAmount=0;

        codeCalculatorFactory =new CodeCalculatorFactory(machineDataDTO.getAlphabetString(), machineDataDTO.getNumberOfRotorsInUse());

        isFinishAllTask= Boolean.FALSE;

    }

//    public void setTaskDoneAmount(AtomicCounter taskDoneAmount) {
//        this.taskDoneAmount = taskDoneAmount;
//    }

    public void setDataConsumer(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;

    }
    public List<DecryptedTask> getTasksForAgentSession(int amount)
    {

        //TODO: fix sync between put and take from blocking queue
        List<DecryptedTask> decryptedTaskList=new ArrayList<>(amount);
        taskQueue.drainTo(decryptedTaskList,amount);
        return decryptedTaskList;
    }

    public void setSetupConfiguration(BruteForceLevel level,int taskSize)
    {
    this.level=level;
    this.taskSize=taskSize;
    totalTaskAmount=0;
    calculateTotalTaskAmount(level);


    }
    public double getTotalTasksAmount() {
        if(totalTaskAmount==0)
            calculateTotalTaskAmount(level);
        return totalTaskAmount;
    }


    public void setCandidateListenerStarter(Runnable startListener)
    {
        this.startListener=startListener;

    }
//
//    public void pause()  {
//        isSystemPause =true;
////        try {
////            taskCreator.checkAccess();
////        } catch (InterruptedException e) {
////            throw new RuntimeException(e);
////        }
//    }
//    public void resume()  {
//        isSystemPause =false;
//        synchronized (pauseLock)
//        {
//         //   System.out.println("Dm:Trying to resume");
//            pauseLock.notifyAll();
//        }
//    }
//    public void stop(){
//        stopFlag=true;
//        isFinishAllTask=true;
//        agents.shutdownNow();
//    }


    public void startCreatingBruteforceTasks(String output)
    {
        this.output=output;
        totalTaskAmount=0;
        isFinishAllTask=false;
        isSystemPause =false;
        stopFlag=false;
        startListener.run();
        taskCreator=new Thread(()-> {
                try {

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

//    static public void doneBruteForceTasks()
//    {
//
//        messageConsumer.accept("Finish running all tasks,finishing update all possible candidate.....");
//        isFinishAllTask=true;
//
//    }



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

                taskQueue.put(new DecryptedTask(currentCode,output, taskSize));
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




package decryptionManager;

import decryptionManager.components.CodeCalculatorFactory;
import decryptionManager.components.Permuter;
import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.GameLevel;
import engineDTOs.DmDTO.SimpleDecryptedTaskDTO;
import engineDTOs.MachineDataDTO;
import engineDTOs.RotorInfoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public class DecryptionManager {
    private  CodeFormatDTO startingCode;
    private final MachineDataDTO machineData;
    private GameLevel level=null;
    private final String allyName;
    private int taskSize=0;
    private final CodeCalculatorFactory codeCalculatorFactory;
    private final BlockingQueue<SimpleDecryptedTaskDTO> taskQueue;
    private final int QUEUE_SIZE=1000;
    private Thread taskCreator;
    private double totalTaskAmount;
    private final AtomicLong taskProducedCounter;


    private AtomicLong PulledCounter=new AtomicLong(0);
    private boolean stopFlag;

    private final AtomicLong idTask=new AtomicLong(1);


    public DecryptionManager(MachineDataDTO machineDataDTO,GameLevel level,String allyName) {

        this.machineData =machineDataDTO;
        this.level=level;
        this.allyName = allyName;
        totalTaskAmount=0;
        taskProducedCounter=new AtomicLong(0);
        codeCalculatorFactory =new CodeCalculatorFactory(machineDataDTO.getAlphabetString(), machineDataDTO.getNumberOfRotorsInUse());
        taskQueue=new ArrayBlockingQueue<>(QUEUE_SIZE);

    }


    public void setStartingCode(CodeFormatDTO startingCode) {
        this.startingCode = CodeFormatDTO.copyOf(startingCode);
    }


    public List<SimpleDecryptedTaskDTO> getTasksForAgentSession(int amount)  {
        List<SimpleDecryptedTaskDTO> decryptedTaskList=new ArrayList<>(amount);

        for (int i = 0; i <amount ; i++) {
            SimpleDecryptedTaskDTO decryptedTaskDTO= null;
            try {

                decryptedTaskDTO = taskQueue.poll(100, TimeUnit.MILLISECONDS);

            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            if(decryptedTaskDTO==null)
                break;
            PulledCounter.incrementAndGet();
            decryptedTaskList.add(decryptedTaskDTO);
        }

        return decryptedTaskList;
    }
    public void setTaskSize(int taskSize) {
        this.taskSize=taskSize;
        totalTaskAmount=0;
        calculateTotalTaskAmount(level);
    }
    public double getTotalTasksAmount() {
        return totalTaskAmount;
    }

    public long getTaskProducedAmount() {
        return taskProducedCounter.get();
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
    public void stop(){
        stopFlag=true;

    }


    public void startCreatingContestTasks()
    {
        stopFlag=false;
         new Thread(()-> {
                try {
                    switch (level) {
                        case EASY:
                            createTaskEasyLevel(startingCode);
                            break;
                        case MEDIUM:

                            createTaskMediumLevel(startingCode);
                            break;
                        case HARD:

                            createTaskHardLevel(startingCode);
                            break;
                        case INSANE:
                            createTaskImpossibleLevel();
                            break;
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error when creating tasks: "+e.getMessage());
                }
        },allyName+ " Task Creator").start();
        System.out.println(allyName+" Finish create all task! ");
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
        CodeFormatDTO currentCode;
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
            createTaskMediumLevel(currentCode);
            currentPermutationIndex=permuteFactory.getNext();
        }


    }

    private void createTaskMediumLevel(CodeFormatDTO codeFormatDTO){
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


        while(temp!=null&&!stopFlag){

            try {
                currentCode=temp;
                taskQueue.put(new SimpleDecryptedTaskDTO(CodeFormatDTO.copyOf(currentCode), taskSize));
                taskProducedCounter.incrementAndGet();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            temp=codeCalculatorFactory.getNextCodeIndexOffset(temp,taskSize);

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
    private double calculateMediumLevelTaskAmount()
    {
        return (machineData.getReflectorIdList().size())*calculateEasyLevelTaskAmount();
    }
    private double calculateHardLevelTaskAmount()
    {
        double totalAmount=calculateFactorial(machineData.getNumberOfRotorsInUse());
        return totalAmount* calculateMediumLevelTaskAmount();
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
    private void calculateTotalTaskAmount(GameLevel level)
    {
            switch (level)
            {
                case EASY:
                    totalTaskAmount= calculateEasyLevelTaskAmount();
                break;
                case MEDIUM:
                    totalTaskAmount= calculateMediumLevelTaskAmount();
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




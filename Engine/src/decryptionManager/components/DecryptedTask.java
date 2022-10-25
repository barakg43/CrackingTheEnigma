package decryptionManager.components;

import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.SimpleDecryptedTaskDTO;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import enigmaEngine.Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;




public class DecryptedTask extends SimpleDecryptedTaskDTO implements Runnable {

    private String agentName;
    private Engine copyEngine=null;
    private Dictionary dictionary=null;
    private List<CandidateDTO> possibleCandidates=null;
    private CodeCalculatorFactory codeCalculatorFactory=null;
    BlockingQueue<TaskFinishDataDTO> successfulDecryption=null;
    private AtomicCounter atomicCounter=null;
    private String cipheredString;

    public DecryptedTask(CodeFormatDTO initialCode, long taskSize) {
        super(initialCode,taskSize);
    }
    public void setupAgentConf(CodeCalculatorFactory codeCalculatorFactory,
                               Engine copyEngine,
                               BlockingQueue<TaskFinishDataDTO> successfulDecryption,
                               Dictionary dictionary,
                               AtomicCounter atomicCounter,
                               String agentName,
                               String cipheredString)
    {
        this.copyEngine = copyEngine;
        possibleCandidates=new ArrayList<>();
        this.atomicCounter = atomicCounter;
        this.dictionary=dictionary;
        this.successfulDecryption=successfulDecryption;
        this.codeCalculatorFactory=codeCalculatorFactory;
        this.agentName=agentName;
        this.cipheredString=cipheredString;
    }
    @Override
    public void run() {
        CodeFormatDTO currentCode=initialCode;
        for (long i = 0; i < taskSize && currentCode!=null ; i++) {

      //     System.out.println(Thread.currentThread().getName() + " is running!");
            copyEngine.setCodeManually(currentCode);
            String processedOutput;
            try {
                processedOutput = copyEngine.processDataInput(cipheredString);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if(dictionary.checkIfAllLetterInDic(processedOutput))
                    {
                        possibleCandidates.add(new CandidateDTO(copyEngine.getCodeFormat(true), processedOutput));
                        System.out.println(currentCode);
//
                        System.out.println("Output: "+ processedOutput+"\n********************************************" );

                    }
                    currentCode= codeCalculatorFactory.getNextCode(currentCode);
                }
        try {
            if(possibleCandidates.size()>0)
                 successfulDecryption.put(new TaskFinishDataDTO(possibleCandidates,agentName));


       // Thread.sleep(DecryptionManager.UI_SLEEP_TIME);//to
         //   currentTaskTimeConsumer.accept(totalTime);
    } catch (InterruptedException ignored) {

       // throw new RuntimeException(e);
        }
      //  atomicCounter.increment();
    }

    @Override
    public String toString() {
        return "DecryptedTask{" +
                "initialCode=" + initialCode +
                ", taskSize=" + taskSize +
                '}';
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
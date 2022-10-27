package decryptionManager.components;

import engineDTOs.CodeFormatDTO;
import engineDTOs.DmDTO.CandidateDTO;
import engineDTOs.DmDTO.SimpleDecryptedTaskDTO;
import engineDTOs.DmDTO.TaskFinishDataDTO;
import enigmaEngine.Engine;


import java.io.IOException;
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
    private String cipheredString;
    private Runnable countersIncrementer;


    public DecryptedTask(CodeFormatDTO initialCode, long taskSize) {

        super(initialCode,taskSize);
    }
    public void setupAgentConf(CodeCalculatorFactory codeCalculatorFactory,
                               Engine copyEngine,
                               BlockingQueue<TaskFinishDataDTO> successfulDecryption,
                               Dictionary dictionary,
                              Runnable countersIncrementer,
                               String agentName,
                               String cipheredString)
    {
        this.copyEngine = copyEngine;
        this.countersIncrementer = countersIncrementer;

        possibleCandidates=new ArrayList<>();

        this.dictionary=dictionary;
        this.successfulDecryption=successfulDecryption;
        this.codeCalculatorFactory=codeCalculatorFactory;
        this.agentName=agentName;
        this.cipheredString=cipheredString;

    }
    @Override
    public void run() {
        CodeFormatDTO currentCode=initialCode;
        try {
            for (long i = 0; i < taskSize && currentCode != null; i++) {


//
                copyEngine.setCodeManually(currentCode);
                String processedOutput;

                processedOutput = copyEngine.processDataInput(cipheredString);


                if (dictionary.checkIfAllLetterInDic(processedOutput)) {
                    possibleCandidates.add(new CandidateDTO(copyEngine.getCodeFormat(true), processedOutput));

//
                    //System.out.println("code: " + currentCode + " Output: " + processedOutput);

                }
                currentCode = codeCalculatorFactory.getNextCode(currentCode);
            }
            try {
                if (possibleCandidates.size() > 0)
                    successfulDecryption.put(new TaskFinishDataDTO(possibleCandidates, agentName));

            } catch (InterruptedException ignored) {

                // throw new RuntimeException(e);
            }

            countersIncrementer.run();
        }catch (Exception e) {
            System.out.println("Error:::"+e.getMessage());
            e.printStackTrace();
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

}
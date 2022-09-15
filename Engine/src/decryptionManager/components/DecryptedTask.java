package decryptionManager.components;

import decryptionManager.DecryptionManager;
import dtoObjects.CodeFormatDTO;
import dtoObjects.DmDTO.CandidateDTO;
import dtoObjects.DmDTO.TaskFinishDataDTO;
import enigmaEngine.Engine;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static decryptionManager.DecryptionManager.fileOutput;
import static decryptionManager.components.AgentsThreadPool.taskNumber;
import static decryptionManager.components.AgentsThreadPool.totalTimeTasks;


public class DecryptedTask implements Runnable {


    private final CodeFormatDTO initialCode;

    private final Engine copyEngine;
    private final double taskSize;
    private Dictionary dictionary;
    private List<CandidateDTO> possibleCandidates;
    private final String cipheredString;
    private CodeCalculatorFactory codeCalculatorFactory;
    BlockingQueue<TaskFinishDataDTO> successfulDecryption;
    public DecryptedTask(CodeFormatDTO initialCode, String cipheredString,CodeCalculatorFactory codeCalculatorFactory,
                         Engine copyEngine, double taskSize, BlockingQueue<TaskFinishDataDTO> successfulDecryption,
                         Dictionary dictionary) {
        this.initialCode = initialCode;
        this.copyEngine = copyEngine;
        this.taskSize = taskSize;
		this.cipheredString=cipheredString;
        this.dictionary=dictionary;
        this.successfulDecryption=successfulDecryption;
        this.codeCalculatorFactory=codeCalculatorFactory;
        possibleCandidates=new ArrayList<>();

        codeCalculatorFactory=new CodeCalculatorFactory(copyEngine.getMachineData().getAlphabetString(),copyEngine.getMachineData().getNumberOfRotorsInUse());

    }

    @Override
    public void run() {

        long startTime=System.nanoTime();
        CodeFormatDTO currentCode=initialCode;

        for (double i = 0; i < taskSize && currentCode!=null ; i++) {

                copyEngine.setCodeManually(currentCode);
                String processedOutput = copyEngine.processDataInput(cipheredString);
                    if(dictionary.checkIfAllLetterInDic(processedOutput))
                    {
                        possibleCandidates.add(new CandidateDTO(copyEngine.getCodeFormat(true),processedOutput));
                        System.out.println(currentCode);
                        System.out.println("********************************************\nOutput " + processedOutput);

                    }
                    currentCode= codeCalculatorFactory.getNextCode(currentCode);
                }
        long totalTime=System.nanoTime()-startTime;
        try {
            if(possibleCandidates.size()>0)
                 successfulDecryption.put(new TaskFinishDataDTO(possibleCandidates,Thread.currentThread().getName(),totalTime));
        totalTimeTasks.set(totalTimeTasks.get()+totalTime);
        Thread.sleep(500);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);}


    }




}
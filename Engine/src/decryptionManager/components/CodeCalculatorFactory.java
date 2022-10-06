package decryptionManager.components;

import engineDTOs.CodeFormatDTO;
import engineDTOs.RotorInfoDTO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CodeCalculatorFactory implements Serializable {

    private Map<Character,Integer> letter2Index =null;
    private char[] index2letter =null;
    private int letterSize;
    private final int numberOfPositions;
    final double MAX_VALUE_OFFSET;
    public CodeCalculatorFactory(String alphabet,int numberOfPositions) {
        addLettersToCal(alphabet);
        this.numberOfPositions=numberOfPositions;
        MAX_VALUE_OFFSET=Math.pow(letterSize,numberOfPositions);
    }
    public char getFirstLetter(){
        return index2letter[0];
    }
    public double getCodeConfAmount()
    {
        return MAX_VALUE_OFFSET;
    }
    private void addLettersToCal(String alphabet)
    {
        letter2Index=new HashMap<>();
        letterSize=alphabet.length();
        index2letter=new char[letterSize];
        for (int i = 0; i < letterSize; i++) {
            index2letter[i]=alphabet.charAt(i);
            letter2Index.put(alphabet.charAt(i),i);
        }
    }

    public CodeFormatDTO getNextCode(CodeFormatDTO currentCode)
    {
        return getNextCodeIndexOffset(currentCode, 1);
    }

    public CodeFormatDTO getNextCodeIndexOffset(CodeFormatDTO initialCode,int offset)//TODO:fix the calculation,for edge cae
    {
        RotorInfoDTO[] rotorInfoDTOS=initialCode.getRotorInfoArray();
        int codeOffset=offset+convertCodePositionToNumber(rotorInfoDTOS);

        if(codeOffset >=MAX_VALUE_OFFSET)
            return null;
        for(int i=0;i<numberOfPositions;i++)
        {
            int innerIndex=codeOffset%letterSize;
           // int distance=((rotorInfoDTOS[i].getDistanceToWindow()-offset) % letterSize + letterSize) % letterSize;
            rotorInfoDTOS[i]=new RotorInfoDTO(rotorInfoDTOS[i].getId(), 0, index2letter[innerIndex]);
            codeOffset=codeOffset/letterSize;
        }
        return new CodeFormatDTO(rotorInfoDTOS, initialCode.getReflectorID(), initialCode.getPlugboardPairDTOList());
    }
    private int convertCodePositionToNumber(RotorInfoDTO[] rotorInfoDTOS)
    {

        int numberInLetterSizeBase=0;
        for (int i = 0; i <numberOfPositions ; i++) {
            int currentIndex=letter2Index.get(rotorInfoDTOS[i].getStatingLetter());
            numberInLetterSizeBase+=currentIndex*Math.pow(letterSize,i);
//            System.out.println("current dig:"+currentIndex*Math.pow(letterSize,i)+" index "+currentIndex);
        }
        //System.out.println("number is:"+numberInLetterSizeBase);
        return numberInLetterSizeBase;
    }



}

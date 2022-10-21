package engineDTOs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MachineDataDTO implements Serializable {

    private final int numberOfRotorsInUse;
    private final int[] rotorsId;
    private final String alphabet;
    private final List<String> reflectorList;
    private final Set<String> dictionaryWords;


    public MachineDataDTO() {
        this.dictionaryWords = null;
        numberOfRotorsInUse = 0;
        rotorsId = null;
        reflectorList = null;
        alphabet="";
    }


    public MachineDataDTO(int numOfRotorsInUse, int[] rotorsIdArray, List<String> reflectorList, String alphabet, Set<String> dictionaryWords) {
        this.numberOfRotorsInUse =numOfRotorsInUse;
        rotorsId =rotorsIdArray;
        this.reflectorList=reflectorList;
        this.alphabet=alphabet;


        this.dictionaryWords = dictionaryWords;
    }

    public int getNumberOfReflectors() {
        assert reflectorList != null;
        return reflectorList.size();
    }
    public int getNumberOfRotorInSystem()
    {
        assert rotorsId != null;
        return rotorsId.length;}


    public List<String> getReflectorIdList(){
        return reflectorList;}

    public String getAlphabetString() {
        return alphabet;}
    public int[] getRotorsId() {
        return rotorsId;
    }

    public int getNumberOfRotorsInUse() {
        return numberOfRotorsInUse;
    }

    @Override
    public String toString() {
        return "dtoObjects.MachineDataDTO{" +
                "numberOfReflectors=" + getNumberOfReflectors() +
                ", numberOfRotorsInUse=" + numberOfRotorsInUse +
                ", rotorsId=" + Arrays.toString(rotorsId) +
                '}';
    }

    public Set<String> getDictionaryWords() {
        return dictionaryWords;
    }
}

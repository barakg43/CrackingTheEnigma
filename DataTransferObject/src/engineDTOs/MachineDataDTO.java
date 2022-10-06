package engineDTOs;

import java.io.Serializable;
import java.util.*;

public class MachineDataDTO implements Serializable {

    private final int numberOfRotorsInUse;
    private final int[] rotorsId;
    private String alphabet;
    private final List<String> reflectorList;
    private Set<String> dictionaryList;
    private List<Character> excludeChars;

    private int numberOfAgents;


    public MachineDataDTO() {
        numberOfRotorsInUse = 0;
        rotorsId = null;
        reflectorList = null;
        dictionaryList=new HashSet<>();
        excludeChars=new ArrayList<>();
    }


    public MachineDataDTO(int numOfRotorsInUse, int[] rotorsIdArray, List<String> reflectorList, String alphabet) {
        this.numberOfRotorsInUse =numOfRotorsInUse;
        rotorsId =rotorsIdArray;
        this.reflectorList=reflectorList;
        this.alphabet=new String(alphabet);


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

}

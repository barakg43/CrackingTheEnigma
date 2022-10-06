package engineDTOs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeFormatDTO implements Serializable , Cloneable{

    private final RotorInfoDTO[] rotorInfoArray;
    private final String reflectorID;
    private final List<PlugboardPairDTO> plugboardPairDTOList;
    protected boolean isCurrentMachineCode;
    public CodeFormatDTO(RotorInfoDTO[] rotorInfoArray, String reflectorID, List<PlugboardPairDTO> plugboardPairDTOList) {
        this.rotorInfoArray = rotorInfoArray;
        this.reflectorID = reflectorID;
        this.plugboardPairDTOList =new ArrayList<>(plugboardPairDTOList);
        isCurrentMachineCode=true;

    }
    public static CodeFormatDTO copyOf(CodeFormatDTO codeFormatDTO)
    {

        RotorInfoDTO[] rotorInfoArray=Arrays.copyOf(codeFormatDTO.rotorInfoArray,codeFormatDTO.rotorInfoArray.length);
        return new CodeFormatDTO(rotorInfoArray, codeFormatDTO.reflectorID,new ArrayList<>(codeFormatDTO.plugboardPairDTOList));

    }
    public RotorInfoDTO[] getRotorInfoArray() {
        return rotorInfoArray;
    }
    public String getReflectorID() {
        return reflectorID;
    }

    public List<PlugboardPairDTO> getPlugboardPairDTOList() {
        return plugboardPairDTOList;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {

        return super.clone();
    }

    @Override
    public String toString() {
        StringBuilder codeFormat=new StringBuilder();
        //example:<45,27,94><A(2)O(5)!(20)><III><A|Z,D|E>
        //<rotor ID(distance from notch to window),...> =<45,27,94>
        codeFormat.append('<');
        for(int i = rotorInfoArray.length-1; i>0; i--)
        {
            codeFormat.append(String.format("%d,", rotorInfoArray[i].getId()));
        }

        codeFormat.append(String.format("%d>", rotorInfoArray[0].getId()));
        //<starting letter leftest,...,starting letter rightest> = <A(2)O(5)!(20)>
        codeFormat.append('<');
        for(int i = rotorInfoArray.length-1; i>0; i--) {
            codeFormat.append(String.format("%c(%d),", rotorInfoArray[i].getStatingLetter(), rotorInfoArray[i].getDistanceToWindow()));
        }
        codeFormat.append(String.format("%c(%d)", rotorInfoArray[0].getStatingLetter(), rotorInfoArray[0].getDistanceToWindow()));

        codeFormat.append('>');
        //<reflector id> = <III>
        codeFormat.append(String.format("<%s>", reflectorID));

        if(!plugboardPairDTOList.isEmpty())
        {
            //<letter-in|letter-out,...> =<A|Z,D|E>
            codeFormat.append('<');
            for (int i = 0; i < plugboardPairDTOList.size()-1; i++)
                codeFormat.append(String.format("%s,", plugboardPairDTOList.get(i)));//print using PlugboardPairDTO toString


            codeFormat.append(String.format("%s>", plugboardPairDTOList.get(plugboardPairDTOList.size()-1)));

        }
        return codeFormat.toString();
    }

    public boolean isCurrentMachineCode() {
        return isCurrentMachineCode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Arrays.hashCode(rotorInfoArray) +
                           plugboardPairDTOList.hashCode()+
                           reflectorID.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CodeFormatDTO other = (CodeFormatDTO) obj;
        return  (Arrays.equals(rotorInfoArray, other.rotorInfoArray)  &&
                (reflectorID.equals(other.reflectorID)) &&
                ( plugboardPairDTOList.equals(other.plugboardPairDTOList)));
    }
}

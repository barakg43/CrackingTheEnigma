package enigmaEngine;

import engineDTOs.CodeFormatDTO;
import engineDTOs.PlugboardPairDTO;
import engineDTOs.RotorInfoDTO;

import java.util.List;

public class CodeFormat extends CodeFormatDTO {

    public CodeFormat(RotorInfoDTO[] rotorInfo, String reflectorID, List<PlugboardPairDTO> plugboardPairDTOList) {
        super(rotorInfo, reflectorID, plugboardPairDTOList);
    }


    public void setIsCurrentCode(boolean isCurrentMachineCode)
    {
        this.isCurrentMachineCode=isCurrentMachineCode;
    }
}

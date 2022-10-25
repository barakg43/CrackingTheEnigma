package engineDTOs.DmDTO;

import engineDTOs.CodeFormatDTO;

public class CandidateDTO {

    private final CodeFormatDTO codeConf;
    private final String output;



    public CandidateDTO(CodeFormatDTO codeConf, String output) {
        this.codeConf = codeConf;
        this.output=output;

    }

    public String getOutput() {
        return output;
    }
    public CodeFormatDTO getCodeConf() {
        return codeConf;
    }


}

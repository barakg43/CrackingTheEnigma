package engineDTOs.DmDTO;

import engineDTOs.CodeFormatDTO;

public class CandidateDTO {

    private final CodeFormatDTO codeConf;
    private final String output;

    private final String userName;

    public CandidateDTO(CodeFormatDTO codeConf, String output,String userName) {
        this.codeConf = codeConf;
        this.output=output;
        this.userName=userName;

    }

    public String getOutput() {
        return output;
    }
    public CodeFormatDTO getCodeConf() {
        return codeConf;
    }

    public String getUserName() {
        return userName;
    }
}

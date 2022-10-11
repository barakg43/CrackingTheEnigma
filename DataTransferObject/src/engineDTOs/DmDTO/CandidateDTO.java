package engineDTOs.DmDTO;

import engineDTOs.CodeFormatDTO;

public class CandidateDTO {

    private final CodeFormatDTO codeConf;
    private final String output;

    private final String allyTeamName;

    public CandidateDTO(CodeFormatDTO codeConf, String output,String allyTeamName) {
        this.codeConf = codeConf;
        this.output=output;
        this.allyTeamName=allyTeamName;

    }

    public String getOutput() {
        return output;
    }
    public CodeFormatDTO getCodeConf() {
        return codeConf;
    }

    public String getAllyTeamName() {
        return allyTeamName;
    }
}

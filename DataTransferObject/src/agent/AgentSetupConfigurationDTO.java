package agent;

import engineDTOs.CodeFormatDTO;

public class AgentSetupConfigurationDTO {

    private final String engineXmlFile;
    private final CodeFormatDTO codeFormatDTO;
    private final String cipheredString;

    public AgentSetupConfigurationDTO(String engineXmlFile, CodeFormatDTO codeFormatDTO, String cipheredString) {
        this.engineXmlFile = engineXmlFile;
        this.codeFormatDTO = codeFormatDTO;
        this.cipheredString = cipheredString;
    }

    public String getEngineXmlFile() {
        return engineXmlFile;
    }

    public CodeFormatDTO getCodeFormatDTO() {
        return codeFormatDTO;
    }

    public String getCipheredString() {
        return cipheredString;
    }
}

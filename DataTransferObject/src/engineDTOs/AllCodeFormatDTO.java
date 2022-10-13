package engineDTOs;

public class AllCodeFormatDTO {

    CodeFormatDTO initialCode;
    CodeFormatDTO currentCode;

    public AllCodeFormatDTO(CodeFormatDTO initialCode, CodeFormatDTO currentCode) {
        this.initialCode = initialCode;
        this.currentCode = currentCode;
    }

    public CodeFormatDTO getInitialCode() {
        return initialCode;
    }

    public CodeFormatDTO getCurrentCode() {
        return currentCode;
    }
}

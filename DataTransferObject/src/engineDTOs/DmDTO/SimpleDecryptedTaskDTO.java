package engineDTOs.DmDTO;

import engineDTOs.CodeFormatDTO;

public class SimpleDecryptedTaskDTO {
    protected final CodeFormatDTO initialCode;
    protected final long taskSize;
    public SimpleDecryptedTaskDTO(CodeFormatDTO initialCode, long taskSize) {
        this.initialCode = initialCode;
        this.taskSize = taskSize;

    }


}

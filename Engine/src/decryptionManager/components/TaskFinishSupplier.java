package decryptionManager.components;

import engineDTOs.DmDTO.TaskFinishDataDTO;

import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

public class TaskFinishSupplier implements Supplier<TaskFinishDataDTO> {
    private final BlockingQueue<TaskFinishDataDTO> finishQueue;
    private final Boolean allTaskDone;
    public TaskFinishSupplier(BlockingQueue<TaskFinishDataDTO> queue, Boolean allTaskDone)
    {
        this.finishQueue=queue;
        this.allTaskDone=allTaskDone;
    }

    @Override
    public TaskFinishDataDTO get() {

        try {
            if(!finishQueue.isEmpty()||!allTaskDone)
                return finishQueue.take();
            else
                return null;
        } catch (InterruptedException ignored) {
           // throw new RuntimeException(e);
        }
        return null;
    }

}

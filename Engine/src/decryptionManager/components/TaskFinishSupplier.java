package decryptionManager.components;

import engineDTOs.DmDTO.TaskFinishDataDTO;

import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

public class TaskFinishSupplier implements Supplier<TaskFinishDataDTO> {
    private final BlockingQueue<TaskFinishDataDTO> finishQueue;

    public TaskFinishSupplier(BlockingQueue<TaskFinishDataDTO> queue)
    {
        this.finishQueue=queue;


    }

    @Override
    public TaskFinishDataDTO get() {

        try {
//            if(!finishQueue.isEmpty()||!isContestEnded)
                return finishQueue.take();
//            else
//                return null;
        } catch (InterruptedException ignored) {
           // throw new RuntimeException(e);
        }
        return null;
    }

}

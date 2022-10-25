package decryptionManager.components;

import UBoatDTO.GameStatus;
import engineDTOs.DmDTO.TaskFinishDataDTO;

import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

public class TaskFinishSupplier implements Supplier<TaskFinishDataDTO> {
    private final BlockingQueue<TaskFinishDataDTO> finishQueue;
    private final Boolean isContestEnded;
    public TaskFinishSupplier(BlockingQueue<TaskFinishDataDTO> queue, Boolean isContestEnded)
    {
        this.finishQueue=queue;

        this.isContestEnded=isContestEnded;
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

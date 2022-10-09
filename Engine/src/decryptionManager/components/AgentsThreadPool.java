package decryptionManager.components;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class AgentsThreadPool extends ThreadPoolExecutor {

    AtomicCounter totalDoneCounter;
    private double totalTaskAmount;
    public AgentsThreadPool(int corePoolSize, int maximumPoolSize,
                            long keepAliveTime, TimeUnit unit,
                            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,AtomicCounter totalDoneCounter) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory);

        this.totalDoneCounter=totalDoneCounter;

    }


    public void setTotalTaskAmount(double taskAmount)
    {
        totalTaskAmount=taskAmount;
    }
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
   //     System.out.println("Perform beforeExecute() logic");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t != null) {
            return;
            //throw new RuntimeException(t);
        }
        totalDoneCounter.increment();
        //if(totalDoneCounter.getValue()==totalTaskAmount)
          //  doneBruteForceTasks();
       // System.out.println("Perform afterExecute() logic");
    }
}

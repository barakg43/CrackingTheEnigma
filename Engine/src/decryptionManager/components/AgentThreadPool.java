package decryptionManager.components;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class AgentThreadPool extends ThreadPoolExecutor {



    public AgentThreadPool(int corePoolSize, int maximumPoolSize,
                           long keepAliveTime, TimeUnit unit,
                           BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory);



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
        //if(totalDoneCounter.getValue()==totalTaskAmount)
          //  doneBruteForceTasks();
       // System.out.println("Perform afterExecute() logic");
    }
}

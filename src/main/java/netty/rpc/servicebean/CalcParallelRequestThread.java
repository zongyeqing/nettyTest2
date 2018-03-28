package netty.rpc.servicebean;

import netty.rpc.core.MessageSendExecutor;
import netty.rpc.servicebean.api.Calculate;

import java.util.concurrent.CountDownLatch;

/**
 * 请填写类注释
 *
 * @author 宗业清
 * @since 2018年03月23日
 */
public class CalcParallelRequestThread implements Runnable {
    
    private CountDownLatch signal;
    private CountDownLatch finish;
    private MessageSendExecutor executor;
    private int taskNumber = 0;
    
    public CalcParallelRequestThread(MessageSendExecutor executor, CountDownLatch signal, CountDownLatch finish, int taskNumber) {
        this.signal = signal;
        this.finish = finish;
        this.taskNumber = taskNumber;
        this.executor = executor;
    }
    
    public void run() {
        try {
            signal.await();
            
            Calculate calc = executor.execute(Calculate.class);
            int add = calc.add(taskNumber, taskNumber);
            System.out.println("calc add result: [" + add + "]");
            
            finish.countDown();
        }catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

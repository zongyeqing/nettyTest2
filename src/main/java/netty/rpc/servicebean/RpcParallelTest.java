package netty.rpc.servicebean;

import netty.rpc.core.MessageSendExecutor;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2018年03月23日
 */
public class RpcParallelTest {

    public static void main(String[] args) throws Exception {
        final MessageSendExecutor executor = new MessageSendExecutor("127.0.0.1:8888");
        //并发度 10000
        int parallel = 10000;

        //开始计时
        StopWatch sw = new StopWatch();
        sw.start();

        CountDownLatch signal = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(parallel);
        
        for (int index = 0; index < parallel; index++) {
            CalcParallelRequestThread client = new CalcParallelRequestThread(executor, signal, finish, index);
            new Thread(client).start();
        }
        
        // 10000个并发线程瞬间发起操作请求
        signal.countDown();
        finish.await();
        
        sw.stop();
        
        String tip = String.format("RPC调用共耗时：[%s]毫秒", sw.getTotalTimeMillis());
        System.out.println(tip);
        executor.stop();
    }
}

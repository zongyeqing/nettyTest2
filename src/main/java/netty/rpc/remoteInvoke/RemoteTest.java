package netty.rpc.remoteInvoke;

import netty.rpc.servicebean.Calculate;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 请填写类注释
 *
 * @author 宗业清
 * @since 2018年03月27日
 */
public class RemoteTest {

    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 1000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(80), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread("rpc client executor");
            }
        });
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Calculate calculate = Remote.getProxy(Calculate.class);
                    int result = calculate.add(new Random().nextInt(1000), new Random().nextInt(1000));
                    System.out.println(result);
                }
            });
        }
    }
}

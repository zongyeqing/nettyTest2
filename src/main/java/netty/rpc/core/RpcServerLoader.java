package netty.rpc.core;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import netty.rpc.serialize.support.RpcSerializeProtocol;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * rpc服务器配置加载
 *
 * @author 宗业清 
 * @since 2017年09月27日
 */
public class RpcServerLoader {

    private volatile static RpcServerLoader rpcServerLoader;
    private final static String DELIMITER = ":";
    private RpcSerializeProtocol seriabizedProtocol = RpcSerializeProtocol.JDKSERIALIZE;

    /** 方法返回到Java虚拟机的可用的处理器数量 */
    private final static int PARALLEL = Runtime.getRuntime().availableProcessors() * 2;
    /** netty nio线程池 */
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(PARALLEL);
    private static ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1);
    private MessageSendHandler messageSendHandler = null;

    //等待netty服务端链路建立通知信号
    private Lock lock = new ReentrantLock();
    private Condition signal = lock.newCondition();

    private RpcServerLoader() {
    }

    /**
     *  并发双重锁定获取RpcServerLoader实例
     * @return
     */
    public static RpcServerLoader getInstanse() {
        if (rpcServerLoader == null) {
            synchronized(RpcServerLoader.class) {
                if (rpcServerLoader == null) {
                    rpcServerLoader = new RpcServerLoader();
                }
            }
        }
        return rpcServerLoader;
    }
    
    public void load(String serverAddress) {
        String[] ipAddr = serverAddress.split(RpcServerLoader.DELIMITER);
        if (ipAddr.length == 2) {
            String host = ipAddr[0];
            int port = Integer.parseInt(ipAddr[1]);
            final InetSocketAddress remoteAddr = new InetSocketAddress(host, port);
            threadPoolExecutor.submit(new MessageSendInitializeTask(eventLoopGroup, remoteAddr, this, seriabizedProtocol));
        }
    }

    public void setSendMessageHandler(MessageSendHandler messageInHandler) {
        try {
            lock.lock();
            this.messageSendHandler = messageInHandler;
            //唤醒所有等待客户端RPC线程
            signal.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    public MessageSendHandler getMessageSendHandler() throws InterruptedException {
        try {
            lock.lock();
            //Netty服务端链路没有建立完毕之前，先挂起等待
            if (messageSendHandler == null) {
                signal.await();
            }
            return messageSendHandler;
        } finally {
            lock.unlock();
        }
    }
    
    public void unLoad() {
        messageSendHandler.close();
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }
    
    public void setSeriabizedProtocol(RpcSerializeProtocol seriabizedProtocol) {
        this.seriabizedProtocol = seriabizedProtocol;
    }
}

package netty.rpc.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.rpc.annotation.RemoteService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.nio.channels.spi.SelectorProvider;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2018年03月22日
 */
public class MessageRecvExecutor implements ApplicationContextAware, InitializingBean {

    private String serverAddress;
    private final static String DELIMITER = ":";

    private Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();

    private static ThreadPoolExecutor threadPoolExecutor;

    public MessageRecvExecutor(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public static void sumbit(Runnable task) {
        if(threadPoolExecutor == null) {
            synchronized(MessageRecvExecutor.class) {
                if(threadPoolExecutor == null) {
                    threadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1);
                }
            }
        }
        threadPoolExecutor.submit(task);
    }

    public void afterPropertiesSet() throws Exception {
        //netty的线程池模型设置成主从线程池模式， 这样可以应对高并发请求
        //当然netty还支持单线程、多线程网络IO模型，可以根据业务需求灵活配置
        ThreadFactory threadFactory = new NamedThreadFactory("NettyRPC ThreadFactory");

        //方法返回java虚拟机可用的处理器数量
        int parallel = Runtime.getRuntime().availableProcessors() * 2;
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup(parallel, threadFactory, SelectorProvider.provider());

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker).channel(NioServerSocketChannel.class).childHandler(new MessageRecvChannelInitializer(handlerMap)).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] ipAddr = serverAddress.split(MessageRecvExecutor.DELIMITER);

            if(ipAddr.length == 2) {
                String host = ipAddr[0];
                int port = Integer.parseInt(ipAddr[1]);
                ChannelFuture future = bootstrap.bind(host, port).sync();
                System.out.printf("Netty RPC Server start success ip:%s port:%d\n", host, port);
                future.channel().closeFuture().sync();
            } else {
                System.out.printf("Netty RPC Server start fail!\n");
            }
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        ScanPackage scanPackage = ctx.getBean(ScanPackage.class);
        List<Class> classes = scanPackage.scan();
        for(Class clazz : classes) {
            Annotation[] annotations = clazz.getAnnotations();
            for(Annotation annotation : annotations) {
                if(annotation.annotationType().equals(RemoteService.class)) {
                    try {
                        Object handler = clazz.newInstance();
                        Class inter = clazz.getInterfaces()[0];
                        String key = inter.getName();
                        handlerMap.put(key, handler);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

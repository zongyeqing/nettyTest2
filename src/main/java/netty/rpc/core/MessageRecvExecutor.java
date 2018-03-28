package netty.rpc.core;

import static com.oracle.jrockit.jfr.ContentType.Bytes;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.rpc.annotation.RemoteService;
import netty.rpc.zookeeper.ServiceRegistry;
import netty.rpc.zookeeper.ZookeeperServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
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

    @Autowired
    private ServiceRegistry serviceRegistry;

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

    @Override
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

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        
        //获取当前计算机的ip地址
        String ip = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("获取当前计算机ip错误");
        }
        //将本项目中含标有RemoteService注解的类添加到远程服务map中
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RemoteService.class);
        if(!CollectionUtils.isEmpty(serviceBeanMap)) {
            for (Map.Entry<String, Object> entry : serviceBeanMap.entrySet()) {
                Object service = entry.getValue();
                String interName = service.getClass().getName();
                handlerMap.put(interName, service);
                //将服务注册到zookeeper中
                serviceRegistry.register(interName, serverAddress);
            }
        }
    }
}

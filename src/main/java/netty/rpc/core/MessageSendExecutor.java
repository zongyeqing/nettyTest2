package netty.rpc.core;

import java.lang.reflect.Proxy;

/**
 * Rpc客户端执行模块
 *
 * @author 宗业清 
 * @since 2017年09月27日
 */
public class MessageSendExecutor{
    
    private RpcServerLoader loader = RpcServerLoader.getInstanse();
    
    public MessageSendExecutor(String serverAddress) {
        loader.load(serverAddress);
    }
    
    public void stop() {
        loader.unLoad();
    }
    
    public <T> T execute(Class<T> rpcInterface) {
        return (T) Proxy.newProxyInstance(
        rpcInterface.getClassLoader(),
        new Class<?>[]{rpcInterface},
        new MessageSendProxy<T>(rpcInterface)
        );
    }
}

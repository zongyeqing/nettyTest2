package netty.rpc.remoteInvoke;

import netty.rpc.core.MessageSendExecutor;
import netty.rpc.zookeeper.ServiceDiscovery;
import netty.rpc.zookeeper.ZooKeeperServiceDiscovery;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 请填写类注释
 *
 * @author 宗业清
 * @since 2018年03月27日
 */
public class Remote implements InvocationHandler {
    
    private Class target;
    
    public Remote(Class target) {
        this.target = target;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceName = target.getName();
        String address = ZooKeeperServiceDiscovery.getInstance().discover(serviceName);
        if(address == null) {
            throw new RuntimeException("没有此远程服务:" + serviceName);
        }
        MessageSendExecutor executor = new MessageSendExecutor(address);
        Object obj = executor.execute(target);
        return method.invoke(obj, args);
    }
    
    public static <T> T  getProxy(Class target) {
        return (T)Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, new Remote(target));
    }
}

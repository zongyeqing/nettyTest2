package netty.rpc.spring.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 请填写类注释
 *
 * @author 宗业清
 * @since 2018年03月28日
 */
public abstract class AbstractInvocationHandler implements InvocationHandler{
    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractInvocationHandler.class);
    
    private static final Map<Class<?>, Object> primitiveValue = new HashMap<>();
    
    static {
        primitiveValue.put(Boolean.TYPE, false);
        primitiveValue.put(Byte.TYPE, 0);
        primitiveValue.put(Character.TYPE, '\u0000');
        primitiveValue.put(Short.TYPE, 0);
        primitiveValue.put(Integer.TYPE, 0);
        primitiveValue.put(Long.TYPE, 0L);
        primitiveValue.put(Double.TYPE, 0.0d);
        primitiveValue.put(Float.TYPE, 0.0f);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        // todo 调用网络
        return result;
    }
}

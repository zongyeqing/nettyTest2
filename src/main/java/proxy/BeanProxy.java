package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2017年09月30日
 */
public class BeanProxy implements InvocationHandler{

    private Object target;

    private BeanProxy(Object target){
        this.target = target;
    }

    public static Object getProxy(Object target){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),new BeanProxy(target));
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getName() + " 方法被调用");
        return method.invoke(target, args);
    }
}

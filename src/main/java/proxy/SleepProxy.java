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
public class SleepProxy implements InvocationHandler{

    private Sleep target;

    public SleepProxy(Sleep target){
        this.target = target;
    }

    public static SleepIn getSleep(Sleep target){
        return (SleepIn)Proxy.newProxyInstance(Sleep.class.getClassLoader(),Sleep.class.getInterfaces(),new SleepProxy(target));
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if(method.getName().equals("sleepBefore")){
            System.out.println("睡觉前方法被调用");
            method.invoke(target,args);
            System.out.println("睡觉前方法调用结束");
        }else if(method.getName().equals("sleeping")){
            System.out.println("睡觉中方法被调用");
            People p = (People)args[0];
            System.out.println("记录日志，姓名:" + p.getName() + "性别: " + p.getSex());
            result =method.invoke(target,args);
            System.out.println("睡觉中方法调用结束");
        }else if(method.getName().equals("sleepAfter")){
            System.out.println("睡觉后方法");
            method.invoke(target, args);
            System.out.println("睡觉后方法调用结束");
        }
        return result;
    }
}

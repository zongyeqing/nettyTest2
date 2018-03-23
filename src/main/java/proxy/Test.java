package proxy;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2017年09月30日
 */
public class Test {
    public static void main(String[] args) {
       Container container = new Container();
       container.setBean();
       Object sleep = container.getBean("sleep");
       SleepIn sleep1 = (SleepIn)sleep;
       sleep1.sleepBefore();
    }
}

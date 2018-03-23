package proxy;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2017年09月30日
 */
public class Sleep implements SleepIn{

    public void sleepBefore(){
        System.out.println("睡觉前");
    }

    public String sleeping(People people){
        String result = "姓名:" + people.getName()+" 性别：" + people.getSex();
        return result;
    }

    public void sleepAfter(){
        System.out.println("睡觉后");
    }
}

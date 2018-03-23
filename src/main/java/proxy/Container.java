package proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2017年09月30日
 */
public class Container {
    private Map<String, Object> beanMap = new HashMap<String, Object>();

    public Object getBean(String beanName){
        Object o = beanMap.get(beanName);
        if(o != null){
            return BeanProxy.getProxy(o);
        }
        return null;
    }

    public void setBean(){
        Sleep sleep = new Sleep();
        beanMap.put("sleep", sleep);
    }
}

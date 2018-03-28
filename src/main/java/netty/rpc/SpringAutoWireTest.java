package netty.rpc;

import netty.rpc.servicebean.api.Calculate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 请填写类注释
 *
 * @author 宗业清 yeqing.zong@ucarinc.com
 * @since 2018年03月28日
 */
@Service
public class SpringAutoWireTest {
    
    @Autowired
    private Calculate calculate;
    
    public void test() {
        System.out.println(calculate.add(5,4));
    }
}

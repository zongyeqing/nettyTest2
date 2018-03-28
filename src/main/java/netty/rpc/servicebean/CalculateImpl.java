package netty.rpc.servicebean;

import netty.rpc.servicebean.api.Calculate;
import org.springframework.stereotype.Service;

/**
 * 请填写类注释
 *
 * @author 宗业清
 * @since 2018年03月23日
 */
@Service
public class CalculateImpl implements Calculate {
    
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}

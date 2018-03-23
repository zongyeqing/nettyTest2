package netty.rpc.servicebean;

import netty.rpc.annotation.RemoteService;

/**
 * 请填写类注释
 *
 * @author 宗业清
 * @since 2018年03月23日
 */
@RemoteService
public class CalculateImpl implements Calculate {
    
    public int add(int a, int b) {
        return a + b;
    }
}

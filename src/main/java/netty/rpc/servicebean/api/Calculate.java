package netty.rpc.servicebean.api;

import netty.rpc.annotation.RemoteService;

/**
 * 请填写类注释
 *
 * @author 宗业清 yeqing.zong@ucarinc.com
 * @since 2018年03月28日
 */
@RemoteService
public interface Calculate {
    int add(int a, int b);
}

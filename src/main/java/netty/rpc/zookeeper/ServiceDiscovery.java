package netty.rpc.zookeeper;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2018年03月26日
 */
public interface ServiceDiscovery {

    /**
     * 服务发现
     * @param name 服务名称
     * @return 服务地址 ip:port
     */
    String discover(String name);
}

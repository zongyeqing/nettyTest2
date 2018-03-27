package netty.rpc.zookeeper;

/**
 * @author 宗叶青 2018/3/25 22:16
 */
public interface ServiceRegistry {

    /**
     * 服务注册
     */
    void register(String serviceName, String serviceAddress);
}

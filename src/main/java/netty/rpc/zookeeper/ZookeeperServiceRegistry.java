package netty.rpc.zookeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author 宗叶青 2018/3/25 22:16
 */
@Component
public class ZookeeperServiceRegistry extends AbstractZKClient implements ServiceRegistry {

    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);

   

    public ZookeeperServiceRegistry() {
        super();
    }
    
    public void register(String serviceName, String serviceAddress) {
        if (!zkClient.exists(REGISTRY_PATH)) {
            zkClient.createPersistent(REGISTRY_PATH);
            LOGGER.info("create zookeeper node: {}", REGISTRY_PATH);
        }
        String servicePath = REGISTRY_PATH + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            LOGGER.info("create zookeeper node: {}", serviceAddress);
        }
        String addressPath = servicePath + "/" + "address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
        LOGGER.info("create zookeeper node: {}", addressNode);
    }
    
}

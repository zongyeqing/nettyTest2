package netty.rpc.zookeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * 请填写类注释
 *
 * @author 宗业清 
 * @since 2018年03月26日
 */
@Component
public class ZooKeeperServiceDiscovery extends AbstractZKClient implements ServiceDiscovery  {

    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);
    
    public ZooKeeperServiceDiscovery() {
        super();
    }
    
    private static ZooKeeperServiceDiscovery serviceDiscovery; 
    
    public static ZooKeeperServiceDiscovery getInstance() {
        if (serviceDiscovery == null) {
            synchronized(ZooKeeperServiceDiscovery.class) {
                if (serviceDiscovery == null) {
                    serviceDiscovery = new ZooKeeperServiceDiscovery();
                }
            }
        }
       return serviceDiscovery;
    }

    @Override
    public String discover(String name) {
        List<String> services = zkClient.getChildren(REGISTRY_PATH);
        for(String service : services) {
            if (service.equals(name)) {
                List<String> addresses = zkClient.getChildren(REGISTRY_PATH + "/" +service);
                if (CollectionUtils.isEmpty(addresses)) {
                    return null;
                }
                int serviceNum = addresses.size();
                String selectedService = null;
                if (serviceNum == 1) {
                    selectedService = addresses.get(0);
                }
                if (serviceNum > 1) {
                    int range = serviceNum ;
                    int random = new Random().nextInt(range);
                    selectedService = addresses.get(random);
                }
                return zkClient.readData(REGISTRY_PATH + "/" + service + "/" + selectedService);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String servicePath = new ZooKeeperServiceDiscovery().discover("netty.rpc.servicebean.Calculate");
        System.out.println(servicePath);
    }
}

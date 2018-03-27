package netty.rpc.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 请填写类注释
 *
 * @author 宗业清
 * @since 2018年03月26日
 */
public class AbstractZKClient {

    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);
    /**
     * zk 地址
     */
    private String zkAddress;
    /**
     * 会话超时时间
     */
    private int sessionTimeOut;
    /**
     * 连接超时时间
     */
    private int connectionTimeOut;

    /** Service注册路径 */
    protected static final String REGISTRY_PATH = "/services";

    /**
     * zookeeper客户端
     */
    protected ZkClient zkClient;
    
    public AbstractZKClient(){
        initProperties();
        zkClient = new ZkClient(zkAddress, sessionTimeOut, connectionTimeOut);
    }

    private void initProperties() {
        Properties properties = null;
        try{
            properties = PropertiesLoaderUtils.loadAllProperties("zookeeper.properties");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        String zkAddress = properties.getProperty("zkAddress");
        sessionTimeOut = Integer.parseInt(properties.getProperty("sessionTimeOut"));
        connectionTimeOut = Integer.parseInt(properties.getProperty("connectionTimeOut"));
        if (StringUtils.isEmpty(zkAddress)) {
            throw new RuntimeException("未获取到zkAddress");
        }
        this.zkAddress = zkAddress;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public int getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }
}

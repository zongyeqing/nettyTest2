package netty.rpc.zookeeper;

import org.I0Itec.zkclient.ZkClient;

/**
 * @author 宗叶青 2018/3/25 22:16
 */
public class ZookeeperServiceRegistry implements ServiceRegistry {

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

    /**
     * zookeeper客户端
     */
    private ZkClient zkClient;

    public ZookeeperServiceRegistry() {
        zkClient = new ZkClient(zkAddress, sessionTimeOut, connectionTimeOut);
    }

    public ZookeeperServiceRegistry(String zkAddress, int sessionTimeOut, int connectionTimeOut) {
        this.zkAddress = zkAddress;
        this.sessionTimeOut = sessionTimeOut;
        this.connectionTimeOut = connectionTimeOut;
        zkClient = new ZkClient(zkAddress, sessionTimeOut, connectionTimeOut);
    }

    public void register() {

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

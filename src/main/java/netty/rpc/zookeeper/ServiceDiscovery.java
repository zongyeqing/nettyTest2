package netty.rpc.zookeeper;

/**
 * 请填写类注释
 *
 * @author 宗业清 yeqing.zong@ucarinc.com
 * @since 2018年03月26日
 */
public interface ServiceDiscovery {

    String discover(String name);
}
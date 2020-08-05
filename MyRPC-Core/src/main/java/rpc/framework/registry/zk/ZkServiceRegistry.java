package rpc.framework.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.framework.registry.ServiceDiscovery;
import rpc.framework.registry.ServiceRegistry;
import util.zk.CuratorHelper;

import java.net.InetSocketAddress;
import java.util.List;

public class ZkServiceRegistry implements ServiceRegistry, ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(CuratorHelper.class);
    private final CuratorFramework zkClient; 
    
    public  ZkServiceRegistry(){
        this.zkClient = CuratorHelper.getZkClient();
        zkClient.start();
    }
    
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        StringBuilder servicePath = new StringBuilder(CuratorHelper.ZK_REGISTER_ROOT_PATH);
        servicePath.append("/");
        servicePath.append(serviceName);
        servicePath.append(inetSocketAddress);
        CuratorHelper.createEphemeralNode(this.zkClient, servicePath.toString());
        logger.info("节点创建成功，节点为:{}", servicePath);
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        List<String> childrenNodes = CuratorHelper.getChildrenNodes(this.zkClient, serviceName);
        // TODO 这里要做负载均衡
        String serviceAddress = childrenNodes.get(0);
        logger.info("成功找到服务地址:{}", serviceAddress);
        return new InetSocketAddress(serviceAddress.split(":")[0], Integer.parseInt(serviceAddress.split(":")[1]));
    }
}

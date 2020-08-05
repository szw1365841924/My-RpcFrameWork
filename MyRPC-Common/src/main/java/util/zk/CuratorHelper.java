package util.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CuratorHelper {

    private static final Logger logger = LoggerFactory.getLogger(CuratorHelper.class);
    
    private static final int SLEEP_MS_BETWEEN_RETRIES = 100;
    private static final int MAX_RETRIES = 3;
    private static final String CONNECT_STRING = "39.99.237.9:2181";
    private static final int CONNECTION_TIMEOUT_MS = 10 * 1000;
    private static final int SESSION_TIMEOUT_MS = 60 * 1000;
    private static final Map<String, List<String>> serviceAddressMap = new ConcurrentHashMap<>();
    
    public static final String ZK_REGISTER_ROOT_PATH = "/MyRPC";

    
    public static CuratorFramework getZkClient(){
        return CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .retryPolicy(new RetryNTimes(MAX_RETRIES, SLEEP_MS_BETWEEN_RETRIES))
                .connectionTimeoutMs(CONNECTION_TIMEOUT_MS)
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .build();
    }
    
    public static void createEphemeralNode(final CuratorFramework zkClient, final String path){
        try {
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            logger.error("发生错误 : ", e);
        }
    }
    
    public static List<String> getChildrenNodes(final CuratorFramework zkClient, final String serviceName){
        if(serviceAddressMap.containsKey(serviceName)){
            return serviceAddressMap.get(serviceName);
        }
        List<String> result = Collections.emptyList();
        String servicePath = CuratorHelper.ZK_REGISTER_ROOT_PATH + "/" + serviceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
            serviceAddressMap.put(serviceName, result);
            registerWatcher(zkClient, serviceName);
        } catch (Exception e) {
            logger.error("发生错误 : ", e);
        }
        return result;
    }

    private static void registerWatcher(CuratorFramework zkClient, String serviceName) {
        String servicePath = CuratorHelper.ZK_REGISTER_ROOT_PATH + "/" + serviceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            serviceAddressMap.put(serviceName, serviceAddresses);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            logger.error("发生错误 : ", e);
        }
    }
}

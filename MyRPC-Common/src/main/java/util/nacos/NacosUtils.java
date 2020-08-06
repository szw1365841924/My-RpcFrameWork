package util.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import enumeration.RpcError;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NacosUtils {

    private static final Logger logger = LoggerFactory.getLogger(NacosUtils.class);
    
    private static final NamingService namingService;
    private static final Set<String> serviceNames = new HashSet<>();
    private static InetSocketAddress address;
    
    private static final String SERVER_ADDR = "39.99.237.9:8848";
    
    static {
        namingService = getNacosNamingService();
    }
    
    public static NamingService getNacosNamingService(){
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            logger.error("连接Nacos时发生错误", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }
    
    public static void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serviceName, inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
            address = inetSocketAddress;
            serviceNames.add(serviceName);
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
    
    public static List<Instance> getAllInstances(String serviceName){
        try {
            return namingService.getAllInstances(serviceName);
        } catch (NacosException e) {
            logger.error("获取服务时有错误发生:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
    
    public static void clearRegistry(){
        if(!serviceNames.isEmpty() && address != null){
            String host = address.getAddress().getHostAddress();
            int port = address.getPort();
            Iterator<String> iterator = serviceNames.iterator();
            while(iterator.hasNext()){
                String serviceName = iterator.next();
                try {
                    namingService.deregisterInstance(serviceName, host, port);
                } catch (NacosException e) {
                    logger.error("注销服务 {} 失败", serviceName, e);
                    throw new RpcException(RpcError.DEREGISTER_SERVICE_FAILED);
                }
            }
        }
    }
}

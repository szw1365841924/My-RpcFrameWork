package rpc.framework.registry.nacos;

import rpc.framework.registry.ServiceRegistry;
import util.nacos.NacosUtils;
import java.net.InetSocketAddress;

public class NacosServiceRegistry implements ServiceRegistry {

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        NacosUtils.registerService(serviceName, inetSocketAddress);
    }
}

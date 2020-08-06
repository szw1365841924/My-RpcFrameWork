package rpc.framework.registry.nacos;

import com.alibaba.nacos.api.naming.pojo.Instance;
import rpc.framework.loadbalancer.LoadBalancer;
import rpc.framework.loadbalancer.impl.RandomLoadBalancer;
import rpc.framework.loadbalancer.impl.RoundRobinLoadBalancer;
import rpc.framework.registry.ServiceDiscovery;
import util.nacos.NacosUtils;

import java.net.InetSocketAddress;
import java.util.List;

public class NacosServiceDiscovery implements ServiceDiscovery {
    
    private final LoadBalancer loadbalance;
    
    public NacosServiceDiscovery(){
        this(new RandomLoadBalancer());
    }
    
    public NacosServiceDiscovery(LoadBalancer loadbalance){
        this.loadbalance = loadbalance;
    }
    
    @Override
    public InetSocketAddress lookupService(String serviceName) {
        List<Instance> list = NacosUtils.getAllInstances(serviceName);
        Instance instance = loadbalance.select(list);
        return new InetSocketAddress(instance.getIp(), instance.getPort());
    }
}

package rpc.framework.loadbalancer.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import rpc.framework.loadbalancer.LoadBalancer;

import java.util.List;

public class RoundRobinLoadBalancer implements LoadBalancer {
    
    private int index = 0;
    
    @Override
    public Instance select(List<Instance> instances) {
        if(index >= instances.size()){
            index %= instances.size();
        }
        return instances.get(index++);
    }
}

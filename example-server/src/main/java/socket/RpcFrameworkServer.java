package socket;

import api.HelloService;
import api.HelloServiceImpl;
import rpc.framework.register.DefaultServiceRegistry;
import rpc.framework.register.ServiceRegistry;
import rpc.framework.server.RpcServer;

public class RpcFrameworkServer {
    
    public static void main(String[] args) {
        HelloServiceImpl service = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(service);
        RpcServer server = new RpcServer(registry);
        server.start(9999);
    }
}

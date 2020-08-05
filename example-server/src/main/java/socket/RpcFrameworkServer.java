package socket;

import api.HelloServiceImpl;
import rpc.framework.provider.impl.ServiceProviderImpl;
import rpc.framework.provider.ServiceProvider;
import rpc.framework.transport.socket.server.RpcSocketServer;

public class RpcFrameworkServer {
    
    public static void main(String[] args) {
        HelloServiceImpl service = new HelloServiceImpl();
        ServiceProvider registry = new ServiceProviderImpl();
        registry.addServiceProvider(service);
        RpcSocketServer server = new RpcSocketServer(registry, 9999);
        server.start();
    }
}

package socket;

import api.HiAndHelloServiceImpl;
import rpc.framework.provider.impl.ServiceProviderImpl;
import rpc.framework.provider.ServiceProvider;
import rpc.framework.transport.socket.server.RpcSocketServer;

public class RpcFrameworkServer {
    
    public static void main(String[] args) {
        HiAndHelloServiceImpl service = new HiAndHelloServiceImpl();
        ServiceProvider registry = new ServiceProviderImpl();
        registry.addServiceProvider(service);
        RpcSocketServer server = new RpcSocketServer(registry, 9999);
        server.start();
    }
}

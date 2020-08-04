package socket;

import api.HelloServiceImpl;
import rpc.framework.register.DefaultServiceRegistry;
import rpc.framework.register.ServiceRegistry;
import rpc.framework.transport.socket.server.RpcSocketServer;

public class RpcFrameworkServer {
    
    public static void main(String[] args) {
        HelloServiceImpl service = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(service);
        RpcSocketServer server = new RpcSocketServer(registry);
        server.start(9999);
    }
}

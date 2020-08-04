package netty;

import api.HelloServiceImpl;
import rpc.framework.register.ServiceRegistry;
import rpc.framework.transport.netty.server.RpcNettyServer;

public class NettyServer {

    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        ServiceRegistry registry = RpcNettyServer.registry;
        registry.register(helloService);
        RpcNettyServer server = new RpcNettyServer();
        server.start(9999);
    }
}

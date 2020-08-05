package netty;

import api.HelloService;
import api.HelloService2;
import api.HelloServiceImpl;
import rpc.framework.transport.netty.server.RpcNettyServer;

public class NettyServer {

    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        RpcNettyServer server = new RpcNettyServer("127.0.0.1", 9999);
        server.publishService(helloService, HelloService.class);
        server.publishService(helloService, HelloService2.class);
        server.start();
    }
}

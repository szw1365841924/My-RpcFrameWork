package netty;

import api.HelloService;
import api.HiService;
import api.HiAndHelloServiceImpl;
import rpc.framework.transport.netty.server.RpcNettyServer;

public class NettyServer {

    public static void main(String[] args) {
        HiAndHelloServiceImpl hiAndHelloService = new HiAndHelloServiceImpl();
        RpcNettyServer server = new RpcNettyServer("127.0.0.1", 9999);

        server.publishService(hiAndHelloService, HiService.class);
        server.publishService(hiAndHelloService, HelloService.class);
        server.start();
    }
}

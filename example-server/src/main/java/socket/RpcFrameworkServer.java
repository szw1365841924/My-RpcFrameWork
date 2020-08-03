package socket;

import api.HelloService;
import api.HelloServiceImpl;
import api.HelloServiceImpl2;
import rpc.framework.RpcServer;

public class RpcFrameworkServer {
    
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer server = new RpcServer();
        server.register(helloService, 9999);
        
        HelloService helloService2 = new HelloServiceImpl2();
        server.register(helloService2, 9998);
    }
}

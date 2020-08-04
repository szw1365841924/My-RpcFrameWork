package socket;

import api.HelloService2;
import api.service.Hello;
import rpc.RpcClient;
import rpc.framework.proxy.RpcClientProxy;
import rpc.framework.transport.socket.client.RpcSocketClient;


public class RpcFrameworkClient {

    public static void main(String[] args) {
        RpcClient client = new RpcSocketClient("127.0.0.1", 9999);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService2 helloService = proxy.getProxy(HelloService2.class);
        String hello = helloService.hi(new Hello("test", "version1.1"));
        System.out.println(hello);
    }
}

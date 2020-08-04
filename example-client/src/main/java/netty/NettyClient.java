package netty;

import api.HelloService;
import api.service.Hello;
import rpc.RpcClient;
import rpc.framework.proxy.RpcClientProxy;
import rpc.framework.transport.netty.client.RpcNettyClient;

public class NettyClient {

    public static void main(String[] args) {
        RpcClient client = new RpcNettyClient("127.0.0.1", 9999);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        String str = helloService.hello(new Hello("test", "version2.0"));
        System.out.println(str);
    }
}

package netty;

import api.HelloService;
import api.HelloService2;
import api.service.Hello;
import rpc.ClientTransport;
import rpc.framework.proxy.RpcClientProxy;
import rpc.framework.transport.netty.client.NettyClientTransport;

import java.net.InetSocketAddress;

public class NettyClient {

    public static void main(String[] args) {
        ClientTransport client = new NettyClientTransport();
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService2 helloService = proxy.getProxy(HelloService2.class);
        String str = helloService.hi(new Hello("test", "version2.0"));
        System.out.println(str);

        HelloService helloService1 = proxy.getProxy(HelloService.class);
        String str2 = helloService1.hello(new Hello("test2", "version2.1"));
        System.out.println(str2);
    }
}

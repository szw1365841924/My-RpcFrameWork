package netty;

import api.HelloService;
import api.HiService;
import api.service.Hello;
import rpc.ClientTransport;
import rpc.framework.proxy.RpcClientProxy;
import rpc.framework.transport.netty.client.NettyClientTransport;

public class NettyClient1 {
    public static void main(String[] args) {
        ClientTransport client = new NettyClientTransport();
        RpcClientProxy proxy = new RpcClientProxy(client);

        // Hello
        HelloService helloService1 = proxy.getProxy(HelloService.class);
        String str2 = helloService1.hello(new Hello("test2", "Hello啊"));
        System.out.println(str2);

        // Hi
        HiService helloService = proxy.getProxy(HiService.class);
        String str = helloService.hi(new Hello("test", "Hi啊"));
        System.out.println(str);
    }
}

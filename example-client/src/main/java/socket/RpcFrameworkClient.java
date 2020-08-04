package socket;

import api.HelloService;
import api.HelloService2;
import api.service.Hello;
import rpc.framework.client.RpcClientProxy;


public class RpcFrameworkClient {

    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9999);
        HelloService2 helloService = proxy.getProxy(HelloService2.class);
        String hello = helloService.hi(new Hello("test", "version1.1"));
        System.out.println(hello);
    }
}

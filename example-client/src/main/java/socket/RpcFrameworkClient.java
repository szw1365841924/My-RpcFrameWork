package socket;

import api.HelloService;
import api.service.Hello;
import rpc.framework.RpcClientProxy;


public class RpcFrameworkClient {

    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9999);
        HelloService helloService = proxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("test", "version1"));
        System.out.println(hello);
    }
}

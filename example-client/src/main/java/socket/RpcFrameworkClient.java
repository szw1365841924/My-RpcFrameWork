package socket;

import api.HiService;
import api.service.Hello;
import rpc.ClientTransport;
import rpc.framework.proxy.RpcClientProxy;
import rpc.framework.transport.socket.client.SocketClientTransport;


public class RpcFrameworkClient {

    public static void main(String[] args) {
        ClientTransport client = new SocketClientTransport("127.0.0.1", 9999);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HiService helloService = proxy.getProxy(HiService.class);
        String hello = helloService.hi(new Hello("test", "version1.1"));
        System.out.println(hello);
    }
}

package rpc.framework.proxy;

import dto.RpcRequest;
import dto.RpcResponse;
import rpc.RpcClient;
import rpc.framework.transport.socket.client.RpcSocketClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {
    private RpcClient client;
    
    public RpcClientProxy(RpcClient client){
        this.client = client;
    }
    
    
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder().methodname(method.getName()).parameters(args).interfacename(method.getDeclaringClass().getName()).paramTypes(method.getParameterTypes()).build();
        return ((RpcResponse) client.sendRPCRequest(request)).getResult();
    }
}

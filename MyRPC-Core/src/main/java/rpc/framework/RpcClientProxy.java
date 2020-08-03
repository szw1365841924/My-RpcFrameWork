package rpc.framework;

import dto.RpcRequest;
import dto.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;
    
    public RpcClientProxy(String host, int port){
        this.host = host;
        this.port = port;
    }
    
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder().methodname(method.getName()).parameters(args).interfacename(method.getDeclaringClass().getName()).paramTypes(method.getParameterTypes()).build();
        Rpclient rpclient = new Rpclient();
        return ((RpcResponse) rpclient.sendRPCRequest(request, this.host, this.port)).getResult();
    }
}

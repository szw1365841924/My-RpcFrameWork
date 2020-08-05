package rpc.framework.proxy;

import dto.RpcRequest;
import dto.RpcResponse;
import rpc.ClientTransport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcClientProxy implements InvocationHandler {
    private ClientTransport client;
    
    public RpcClientProxy(ClientTransport client){
        this.client = client;
    }
    
    
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder()
                .methodname(method.getName())
                .parameters(args)
                .interfacename(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .build();
        return ((RpcResponse) client.sendRPCRequest(request)).getResult();
    }
}

package rpc;

public interface RpcServer {
    
    void start();
    
    <T> void publishService(Object obj, Class<T> clazz);
}

package rpc.framework.register;

public interface ServiceRegistry {
    
    <T> void register(T service);
    Object getService(String serviceName);
}

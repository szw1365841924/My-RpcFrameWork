package rpc.framework.ThreadPool;

import dto.RpcRequest;
import enumeration.RpcError;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    
    public Object handle(Object service, RpcRequest request){
        Object result = null;
        try {
            result = invokeTargerMethod(service, request);
            logger.info("服务:{} 成功调用方法:{}", request.getInterfacename(), request.getMethodname());
        }catch (Exception e){
            logger.error("调用或发送时有错误发生：", e);
        }
        return result;
    }
    
    private Object invokeTargerMethod(Object service, RpcRequest request) throws InvocationTargetException, IllegalAccessException {
        Method method;
        try {
            method = service.getClass().getMethod(request.getMethodname(), request.getParamTypes());
        } catch (NoSuchMethodException e) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return method.invoke(service, request.getParameters());
    }
}

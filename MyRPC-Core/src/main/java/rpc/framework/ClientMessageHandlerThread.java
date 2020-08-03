package rpc.framework;

import dto.RpcRequest;
import dto.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class ClientMessageHandlerThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientMessageHandlerThread.class);
    
    private Socket socket;
    private Object service;
    public ClientMessageHandlerThread(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        try(ObjectInputStream inputStream = new ObjectInputStream(this.socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(this.socket.getOutputStream())) {
            RpcRequest request = (RpcRequest) inputStream.readObject();
            Object result = invokeTargetMethod(request);
            outputStream.writeObject(result);
            outputStream.flush();
        } catch (Exception e) {
            logger.error("发生错误: ", e);
        }
    }

    private Object invokeTargetMethod(RpcRequest request) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = Class.forName(request.getInterfacename());
        if(!clazz.isAssignableFrom(this.service.getClass())){
            return RpcResponse.fail("调用失败");
        }
        Method method = this.service.getClass().getMethod(request.getMethodname(), request.getParamTypes());
        if(null == method){
            return RpcResponse.fail("调用失败");
        }
        return RpcResponse.success(method.invoke(this.service, request.getParameters()));
    }
}

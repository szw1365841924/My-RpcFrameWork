package rpc.framework.ThreadPool;

import dto.RpcRequest;
import dto.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.framework.provider.ServiceProvider;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(WorkerRunnable.class);
    
    private Socket socket;
    private ServiceProvider registry;
    private RequestHandler requestHandler;
    public WorkerRunnable(Socket socket, ServiceProvider registry, RequestHandler requestHandler) {
        this.socket = socket;
        this.registry = registry;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        try(ObjectInputStream inputStream = new ObjectInputStream(this.socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(this.socket.getOutputStream())) {
            RpcRequest request = (RpcRequest) inputStream.readObject();
            Object service = registry.getServiceProvider(request.getInterfacename());
            Object result = requestHandler.handle(service, request);
            outputStream.writeObject(RpcResponse.success(result, request.getRequestId()));
            outputStream.flush();
        } catch (Exception e) {
            logger.error("发生错误: ", e);
        }
    }
}

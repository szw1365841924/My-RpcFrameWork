package rpc.framework.transport.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.RpcServer;
import rpc.framework.threadpool.RequestHandler;
import rpc.framework.threadpool.WorkerRunnable;
import rpc.framework.provider.ServiceProvider;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class RpcSocketServer implements RpcServer {
    
    private static final Logger logger = LoggerFactory.getLogger(RpcSocketServer.class);
    
    private final int port;
    private final ExecutorService threadPool;
    private final ServiceProvider registry; 
    
    public RpcSocketServer(ServiceProvider registry, int port){
        int corePoolsize = 10;
        int maximumPoolSizeSize = 100;
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);
        long keepAliveTime = 1;
        ThreadPoolExecutor.DiscardOldestPolicy handler = new ThreadPoolExecutor.DiscardOldestPolicy();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(corePoolsize, maximumPoolSizeSize,
                keepAliveTime, TimeUnit.SECONDS, queue, threadFactory, handler);
        this.registry = registry;
        this.port = port;
    }

    @Override
    public void start(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务注册成功......");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                logger.info("客户端连接......");
                threadPool.execute(new WorkerRunnable(socket, registry, new RequestHandler()));
            }
        } catch (Exception e) {
            logger.error("发生错误: ", e);
        }
    }

    @Override
    public <T> void publishService(Object obj, Class<T> clazz) { }
}

package rpc.framework.transport.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.RpcServer;
import rpc.framework.ThreadPool.RequestHandler;
import rpc.framework.ThreadPool.WorkerRunnable;
import rpc.framework.register.ServiceRegistry;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class RpcSocketServer implements RpcServer {
    
    private static final Logger logger = LoggerFactory.getLogger(RpcSocketServer.class);

    private final ExecutorService threadPool;
    private final ServiceRegistry registry; 
    
    public RpcSocketServer(ServiceRegistry registry){
        int corePoolsize = 10;
        int maximumPoolSizeSize = 100;
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);
        long keepAliveTime = 1;
        ThreadPoolExecutor.DiscardOldestPolicy handler = new ThreadPoolExecutor.DiscardOldestPolicy();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(corePoolsize, maximumPoolSizeSize,
                keepAliveTime, TimeUnit.SECONDS, queue, threadFactory, handler);
        this.registry = registry;
    }

    @Override
    public void start(int port){
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
}

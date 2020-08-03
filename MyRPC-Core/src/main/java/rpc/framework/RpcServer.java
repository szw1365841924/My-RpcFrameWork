package rpc.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class RpcServer {
    private ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    public RpcServer(){
        int corePoolsize = 10;
        int maximumPoolSizeSize = 100;
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);
        long keepAliveTime = 1;
        ThreadPoolExecutor.DiscardOldestPolicy handler = new ThreadPoolExecutor.DiscardOldestPolicy();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(corePoolsize, maximumPoolSizeSize,
                keepAliveTime, TimeUnit.SECONDS, queue, threadFactory, handler);
    }
    
    public void register(Object service, int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务注册成功......");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                logger.info("客户端连接......");
                threadPool.execute(new ClientMessageHandlerThread(socket, service));
            }
        } catch (Exception e) {
            logger.error("发生错误: ", e);
        }
    }
}

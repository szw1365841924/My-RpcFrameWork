package util.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import util.concurrent.config.ThreadPoolConfig;

import java.util.Map;
import java.util.concurrent.*;

public class ThreadPoolUtils {

    private static Map<String, ExecutorService> threadPools = new ConcurrentHashMap<>();
    
    private ThreadPoolUtils(){
        
    }
    
    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix){
        ThreadPoolConfig config = new ThreadPoolConfig();
        return createCustomThreadPoolIfAbsent(threadNamePrefix, config, false);
    }
    
    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix, ThreadPoolConfig config){
        return createCustomThreadPoolIfAbsent(threadNamePrefix, config, false);
    }
    
    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix, ThreadPoolConfig config, boolean daemon){
        ExecutorService threadPool = threadPools.computeIfAbsent(threadNamePrefix, k -> createThreadPool(config, threadNamePrefix, daemon));
        if(threadPool.isShutdown() || threadPool.isTerminated()){
            threadPools.remove(threadNamePrefix);
            threadPool = createThreadPool(config, threadNamePrefix, daemon);
            threadPools.put(threadNamePrefix, threadPool);
        }
        return threadPool;
    }
    
    private static ExecutorService createThreadPool(ThreadPoolConfig config, String threadNamePrefix, boolean daemon){
        ThreadFactory threadFactory = createThreadFactory(daemon, threadNamePrefix);
        return new ThreadPoolExecutor(config.getCorePoolSize(), config.getMaximumPoolSize(),
                config.getKeepAliveTime(), config.getUnit(), config.getWorkQueue(),
                threadFactory);
    }
    
    private static ThreadFactory createThreadFactory(Boolean daemon, String threadNamePrefix){
        if(threadNamePrefix != null){
            if(daemon != null){
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .setDaemon(daemon)
                        .build();
            }else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }
    
    public static void shutDownAllThreadPool() {
        threadPools.entrySet().parallelStream().forEach(entry -> {
            ExecutorService service = entry.getValue();
            service.shutdown();
            try {
                service.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                service.shutdownNow();
            }
        });
    }
}

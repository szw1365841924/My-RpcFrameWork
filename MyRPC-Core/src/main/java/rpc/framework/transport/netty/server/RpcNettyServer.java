package rpc.framework.transport.netty.server;

import dto.RpcRequest;
import dto.RpcResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.RpcServer;
import rpc.framework.provider.ServiceProvider;
import rpc.framework.provider.impl.ServiceProviderImpl;
import rpc.framework.registry.ServiceRegistry;
import rpc.framework.registry.nacos.NacosServiceRegistry;
import rpc.framework.serialize.impl.KryoSerializer;
import rpc.framework.transport.netty.codec.CommonDecoder;
import rpc.framework.transport.netty.codec.CommonEncoder;

import java.net.InetSocketAddress;

public class RpcNettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(RpcNettyServer.class);
    
    private String host;
    private  int port;
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;
    
    public RpcNettyServer(String host, int port){
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
    }
    
    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        KryoSerializer kryoSerializer = new KryoSerializer();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new CommonEncoder(kryoSerializer, RpcResponse.class));
                            pipeline.addLast(new CommonDecoder(kryoSerializer, RpcRequest.class));
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
                ChannelFuture future = serverBootstrap.bind(port).sync();
                future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(this.host, this.port));
        serviceProvider.addServiceProvider(service);
    }
}

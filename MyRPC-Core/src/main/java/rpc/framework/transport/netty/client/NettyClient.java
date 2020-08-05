package rpc.framework.transport.netty.client;

import dto.RpcRequest;
import dto.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.framework.serialize.impl.KryoSerializer;
import rpc.framework.transport.netty.codec.CommonDecoder;
import rpc.framework.transport.netty.codec.CommonEncoder;

public class NettyClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    
    private static final Bootstrap b;
    private static final EventLoopGroup eventLoopGroup;
    
    static {
        eventLoopGroup = new NioEventLoopGroup();
        b = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new CommonDecoder(kryoSerializer, RpcResponse.class));
                        ch.pipeline().addLast(new CommonEncoder(kryoSerializer, RpcRequest.class));
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
    }

    private NettyClient() { }
    
    public static void close(){
        logger.info("call close method");
        eventLoopGroup.shutdownGracefully();
    }

    public static Bootstrap initializeBootstrap() {
        return b;
    }
}

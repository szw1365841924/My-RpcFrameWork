package rpc.framework.transport.netty.client;

import dto.RpcRequest;
import dto.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.RpcClient;
import rpc.framework.serialize.Impl.KryoSerializer;
import rpc.framework.transport.netty.codec.CommonDecoder;
import rpc.framework.transport.netty.codec.CommonEncoder;

public class RpcNettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcNettyClient.class);

    private String host;
    private int port;
    private static final Bootstrap bootstrap;
    
    public RpcNettyClient(String host, int port){
        this.host = host;
        this.port = port;
    }
    
    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        KryoSerializer kryoSerializer = new KryoSerializer();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new CommonDecoder(kryoSerializer, RpcResponse.class));
                        pipeline.addLast(new CommonEncoder(kryoSerializer, RpcRequest.class));
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }
    
    @Override
    public Object sendRPCRequest(RpcRequest rpcRequest) {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            logger.info("客户端连接到服务器 {}:{}", host, port);
            Channel channel = future.channel();
            if(channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if(future1.isSuccess()){
                        logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                    } else {
                        logger.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse;
            }
        } catch (InterruptedException e) {
            logger.error("发送消息时有错误发生: ", e);
        }
        return null;
    }
}

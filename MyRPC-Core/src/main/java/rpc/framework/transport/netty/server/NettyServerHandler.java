package rpc.framework.transport.netty.server;

import dto.RpcRequest;
import dto.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.framework.ThreadPool.RequestHandler;
import rpc.framework.register.DefaultServiceRegistry;
import rpc.framework.register.ServiceRegistry;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    
    private static RequestHandler requestHandler = new RequestHandler();
    private ServiceRegistry registry;
    
    public NettyServerHandler(ServiceRegistry registry){
        this.registry = registry;
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            logger.info("服务器接收到请求: {}", msg);
            RpcRequest request = (RpcRequest) msg;
            String name = request.getInterfacename();
            Object service = this.registry.getService(name);
            Object result = requestHandler.handle(service, request);
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}

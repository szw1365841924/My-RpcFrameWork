package rpc.framework.transport.netty.client;

import checker.RpcMessageChecker;
import dto.RpcRequest;
import dto.RpcResponse;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.ClientTransport;
import rpc.framework.registry.ServiceDiscovery;
import rpc.framework.registry.nacos.NacosServiceRegistry;
import java.net.InetSocketAddress;

public class NettyClientTransport implements ClientTransport {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientTransport.class);
    private final ServiceDiscovery serviceDiscovery;
    
    public NettyClientTransport(){
        this.serviceDiscovery = new NacosServiceRegistry();
    }
    
    @Override
    public Object sendRPCRequest(RpcRequest rpcRequest) {
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfacename());
            // TODO 这里要考虑当前选中的服务无法正常运行, 应该遍历所有已注册的服务找到一个可以正常运行的
            Channel channel = ChannelProvider.get(inetSocketAddress);
            if(channel.isActive()){
                channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                   if(future.isSuccess()){
                       logger.info("客户端发送消息: {}", rpcRequest);
                   } else {
                       future.channel().close();
                       logger.error("客户端发送失败: ", future.cause());
                   }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RpcResponse rpcResponse = channel.attr(key).get();
                RpcMessageChecker.check(rpcRequest, rpcResponse);
                return rpcResponse;
            }
        } catch (Exception e){
            logger.error("发生错误", e);
        }
        return null;
    }
}

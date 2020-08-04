package rpc.framework.transport.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import rpc.framework.serialize.Serializer;

@AllArgsConstructor
public class CommonEncoder extends MessageToByteEncoder<Object> {
    
    private final Serializer serializer;
    private final Class<?> genericClass;
    
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if(genericClass.isInstance(o)) {
            byte[] bytes = serializer.serialize(o);
            int length = bytes.length;
            byteBuf.writeInt(length);
            byteBuf.writeBytes(bytes);
        }
    }
}

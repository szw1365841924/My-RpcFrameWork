package rpc.framework.transport.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.framework.serialize.Serializer;
import java.util.List;

@AllArgsConstructor
public class CommonDecoder extends ByteToMessageDecoder {
    
    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    
    private final Serializer serializer;
    private final Class<?> genericClass;
    
    private static final int BODY_LENGTH = 4;
    
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() >= BODY_LENGTH){
            in.markReaderIndex();
            int length = in.readInt();
            if(length < 0 || in.readableBytes() < 0){
                logger.error("数据长度出错");
                return;
            }
            if(in.readableBytes() < length){
                in.resetReaderIndex();
                return;
            }
            byte[] bytes = new byte[length];
            in.readBytes(bytes);
            Object obj = serializer.deserialize(bytes, genericClass);
            out.add(obj);
            logger.info("对象解码成功");
        }
    }
}

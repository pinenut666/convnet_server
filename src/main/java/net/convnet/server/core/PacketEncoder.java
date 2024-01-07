package net.convnet.server.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.convnet.server.Constants;
import net.convnet.server.protocol.Protocol;
import net.convnet.server.protocol.ProtocolFactory;
import net.convnet.server.protocol.ResponseReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CVN数据包编码器
 *
 * @author Administrator
 * @date 2024/01/06
 */
@Sharable
final class PacketEncoder extends MessageToByteEncoder<ResponseReader> {
   private static final Logger LOG = LoggerFactory.getLogger(PacketEncoder.class);
   private final ProtocolFactory protocolFactory;

   PacketEncoder(ProtocolFactory protocolFactory) {
      this.protocolFactory = protocolFactory;
   }

   @Override
   protected void encode(ChannelHandlerContext ctx, ResponseReader reader, ByteBuf out) throws Exception {
      LOG.debug("Response send is " + reader);
      int start = out.writerIndex();
      //out.writeInt(0) 的作用是在传输流中写入一个 int 类型的值 0。
      // 它的作用在于在编码数据包之前，先向传输流中写入一个占位值，占用 4 个字节的空间。
      out.writeInt(0);
      Protocol protocol = this.protocolFactory.getProtocol(reader.getVersion());
      protocol.encode(reader, out);
      int len = out.readableBytes() - 4;
      out.markWriterIndex();
      out.writerIndex(start);
      out.writeInt(len);
      out.resetWriterIndex();
      if (LOG.isDebugEnabled()) {
         LOG.debug("Send packet: [" + out.toString(Constants.CHARSET) + "]");
      }

      Long writeBytes = ctx.channel().attr(Constants.WRITE_BYTES_KEY).get();
      if (writeBytes == null) {
         writeBytes = 0L;
      }

      ctx.channel().attr(Constants.WRITE_BYTES_KEY).set(writeBytes + (long)len);
   }
}

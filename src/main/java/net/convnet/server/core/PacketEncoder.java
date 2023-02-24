package net.convnet.server.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.convnet.server.Constants;
import net.convnet.server.protocol.ProtocolFactory;
import net.convnet.server.protocol.ResponseReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
final class PacketEncoder extends MessageToByteEncoder<ResponseReader> {
   private static final Logger LOG = LoggerFactory.getLogger(PacketEncoder.class);
   private final ProtocolFactory protocolFactory;

   PacketEncoder(ProtocolFactory protocolFactory) {
      this.protocolFactory = protocolFactory;
   }

   @Override
   protected void encode(ChannelHandlerContext ctx, ResponseReader reader, ByteBuf out) throws Exception {
      int start = out.writerIndex();
      out.writeInt(0);
      this.protocolFactory.getProtocol(reader.getVersion()).encode(reader, out);
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

package net.convnet.server.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ByteProcessor;
import net.convnet.server.Constants;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
final class HandshakeHandle extends ChannelInboundHandlerAdapter {
   private static AttributeKey<Boolean> HANDSHAKE_KEY = AttributeKey.newInstance("Handshake");
   private static final Logger LOG = LoggerFactory.getLogger(HandshakeHandle.class);

   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      ByteBuf in = (ByteBuf)msg;
      if (in.isReadable()) {
         if (LOG.isDebugEnabled()) {
            final StringBuilder sb = new StringBuilder(in.readableBytes());
            sb.append("\n---------------------------------------------------------------------------------------------\n");
            in.forEachByte(new ByteProcessor() {
               public boolean process(byte value) throws Exception {
                  sb.append(Hex.encodeHex(new byte[]{value})).append(" ");
                  return true;
               }
            });
            sb.append("\n----------------------------------------------------------------------------------------------\n");
            LOG.info("Packet |" + sb.toString() + "|");
            LOG.info("Packet string >>|" + in.toString(Constants.CHARSET) + "|<<");
            sb.append("\n----------------------------------------------------------------------------------------------\n");
         }

         ctx.fireChannelRead(msg);
      }
   }
}

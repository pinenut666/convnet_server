package net.convnet.server.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import net.convnet.server.Constants;
import net.convnet.server.protocol.Protocol;
import net.convnet.server.protocol.Request;
import net.convnet.server.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class PacketDecoder extends LengthFieldBasedFrameDecoder {
   private static final Logger LOG = LoggerFactory.getLogger(PacketDecoder.class);
   private SessionManager sessionManager;

   PacketDecoder(SessionManager sessionManager) {
      super(51200, 0, 4, 0, 4);
      this.sessionManager = sessionManager;
   }

   @Override
   protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
      ByteBuf frame;
      try {
         frame = (ByteBuf)super.decode(ctx, in);
      } catch (Exception var5) {
         LOG.info("Frame decode error", var5);
         ctx.channel().close();
         return null;
      }

      Long readBytes = (Long)ctx.channel().attr(Constants.READ_BYTES_KEY).get();
      if (readBytes == null) {
         readBytes = 0L;
      }

      if (frame == null) {
         return null;
      } else {
         ctx.channel().attr(Constants.READ_BYTES_KEY).set(readBytes + (long)frame.readableBytes());
         if (LOG.isDebugEnabled()) {
            LOG.debug("Receive packet [" + frame.toString(Constants.CHARSET) + "]");
         }
         // 调用sessionManager对象的getProtocol方法，获取当前通道对应的协议对象，并使用该对象将ByteBuf对象解码为具体的响应对象
         Protocol protocol = this.sessionManager.getProtocol(ctx.channel());
         Request request = protocol.decode(frame);
         LOG.debug("Request getting is " + request.toString());
         return request;
      }
   }
}

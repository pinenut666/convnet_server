package net.convnet.server.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.convnet.server.protocol.*;
import net.convnet.server.session.Session;
import net.convnet.server.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ConvnetHandler extends ChannelInboundHandlerAdapter {
   private static final Logger LOG = LoggerFactory.getLogger(ConvnetHandler.class);
   private final ProtocolFactory protocolFactory;
   private final SessionManager sessionManager;
   private final FilterChain filterChain;

   ConvnetHandler(ProtocolFactory protocolFactory, SessionManager sessionManager, FilterChain filterChain) {
      this.protocolFactory = protocolFactory;
      this.sessionManager = sessionManager;
      this.filterChain = filterChain;
   }

   public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      this.sessionManager.createAnonymousSession(ctx.channel());
   }

   public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
      Session session = this.sessionManager.getSession(ctx.channel());
      if (session != null) {
         session.destory();
      }

   }

   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      Request request = (Request)msg;
      Cmd cmd = request.getCmd();
      if (cmd == Cmd.ERROR) {
         ctx.write(request);
      } else {
         Session session = this.sessionManager.getSession(ctx.channel());
         if (session != null && (session.isLogin() || Cmd.ANONYMOUS_CMDS.contains(cmd))) {
            Protocol protocol = this.protocolFactory.getProtocol(request.getVersion());
            Response response = protocol.createResponse(cmd);

            try {
               this.filterChain.doFilter(session, request, response);
            } catch (Throwable var9) {
               if (LOG.isInfoEnabled()) {
                  LOG.error("Process cmd [" + cmd + "] error", var9);
               }

               response.setSuccess(false);
            }

            if (response.needOutput()) {
               ctx.write(response);
            }

         }
      }
   }

   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
      ctx.flush();
   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
      if (LOG.isInfoEnabled()) {
         LOG.error("Serve error", e);
      }

   }
}

package net.convnet.server.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.Lifecycle;

import java.net.InetSocketAddress;
import java.util.Arrays;

public class UdpPortDetectServer implements Lifecycle, InitializingBean, DisposableBean {
   private static final Logger LOG = LoggerFactory.getLogger(Server.class);
   private EventLoopGroup group;
   private boolean running;
   private int[] ports;

   public void setPorts(int[] ports) {
      this.ports = ports;
   }

   public void start() {
      if (this.running) {
         LOG.info("Already running");
      } else {
         LOG.info("------ Attempt to start udpPortDetectServer server on [{}] ------", Arrays.toString(this.ports));

         try {
            Bootstrap bootstrap = new Bootstrap();
            ((Bootstrap)bootstrap.group(this.group)).channel(NioDatagramChannel.class);
            int[] arr$ = this.ports;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               int port = arr$[i$];
               bootstrap.handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                  protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
                     ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(String.valueOf(((InetSocketAddress)packet.sender()).getPort()), CharsetUtil.UTF_8), (InetSocketAddress)packet.sender()));
                  }
               });
               bootstrap.bind(port).sync();
            }

            LOG.info("------ Convnet server listening on [{}:{}] ready to serve ------", Arrays.toString(this.ports));
            this.running = true;
         } catch (Throwable var6) {
            LOG.error("Server startup error", var6);
         }

      }
   }

   public void stop() {
      if (this.running) {
         this.group.shutdownGracefully();
         LOG.info("---- Convnet udpPortDetectServer shutdown successfully ----", Arrays.toString(this.ports));
      }

   }

   public boolean isRunning() {
      return this.running;
   }

   public void destroy() throws Exception {
      this.stop();
   }

   public void afterPropertiesSet() throws Exception {
      this.group = new NioEventLoopGroup();
      this.start();
   }
}

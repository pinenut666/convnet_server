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

import java.util.Arrays;

public class UdpPortDetectServer implements Lifecycle, InitializingBean, DisposableBean {
   private static final Logger LOG = LoggerFactory.getLogger(Server.class);
   private EventLoopGroup group;
   private boolean running;
   private int[] ports;

   public void setPorts(int[] ports) {
      this.ports = ports;
   }

   @Override
   public void start() {
      if (this.running) {
         LOG.info("Already running");
      } else {
         LOG.info("------ Attempt to start udpPortDetectServer server on [{}] ------", Arrays.toString(this.ports));

         try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this.group).channel(NioDatagramChannel.class);
            int[] portslist = this.ports;

            for (int port : portslist) {
               bootstrap.handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                  @Override
                  protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
                     ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(String.valueOf(packet.sender().getPort()), CharsetUtil.UTF_8), packet.sender()));
                  }
               });
               bootstrap.bind(port).sync();
            }

            LOG.info("------ Convnet server listening on [{}:{}] ready to serve ------", Arrays.toString(this.ports));
            this.running = true;
         } catch (Throwable e) {
            LOG.error("Server startup error", e);
         }

      }
   }

   @Override
   public void stop() {
      if (this.running) {
         this.group.shutdownGracefully();
         LOG.info("---- Convnet udpPortDetectServer shutdown successfully ----", Arrays.toString(this.ports));
      }

   }

   @Override
   public boolean isRunning() {
      return this.running;
   }

   @Override
   public void destroy() throws Exception {
      this.stop();
   }

   @Override
   public void afterPropertiesSet() throws Exception {
      this.group = new NioEventLoopGroup();
      this.start();
   }
}

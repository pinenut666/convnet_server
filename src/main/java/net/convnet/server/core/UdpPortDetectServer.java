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
            // 创建Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this.group).channel(NioDatagramChannel.class);
            // 创建UDP辅助握手（2个端口）
            int[] portslist = this.ports;
            // 对多个端口启动辅助握手
            for (int port : portslist) {
               bootstrap.handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                  @Override
                  //辅助握手端口所做的功能：当收到请求时，将对方请求的端口号封装并发送回发送者。
                  //此时用户端将会得到自己NAT转发后的端口号。（理论上，地址是很容易得到的。）
                  protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
                     ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(String.valueOf(packet.sender().getPort()), CharsetUtil.UTF_8), packet.sender()));
                  }
               });
               //绑定
               bootstrap.bind(port).sync();
            }

            LOG.info("------ Convnet server listening on [{}:{}] ready to serve ------", "0.0.0.0",Arrays.toString(this.ports));
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
         LOG.info("---- Convnet udpPortDetectServer on port {} shutdown successfully ----", Arrays.toString(this.ports));
      }

   }

   @Override
   public boolean isRunning() {
      return this.running;
   }

   @Override
   public void destroy() {
      this.stop();
   }

   @Override
   public void afterPropertiesSet() {
      this.group = new NioEventLoopGroup();
      this.start();
   }
}

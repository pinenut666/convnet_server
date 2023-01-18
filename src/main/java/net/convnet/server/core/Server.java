package net.convnet.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.Lifecycle;

public final class Server implements Lifecycle, InitializingBean, DisposableBean {
   private static final Logger LOG = LoggerFactory.getLogger(Server.class);
   private EventLoopGroup bossGroup;
   private EventLoopGroup workerGroup;
   private boolean running;
   private ServerInitializer serverInitializer;
   private String serverAddress;
   private int serverPort;
   private int poolSize;

   public void setServerInitializer(ServerInitializer serverInitializer) {
      this.serverInitializer = serverInitializer;
   }

   public void setListen(String listen) {
      String[] arr = StringUtils.split(listen, ":");
      this.serverAddress = arr[0];
      this.serverPort = Integer.parseInt(arr[1]);
   }

   public void setPoolSize(int poolSize) {
      this.poolSize = poolSize;
   }

   public void start() {
      if (this.running) {
         LOG.info("Already running");
      } else {
         LOG.info("------ Attempt to start convnet server on [{}:{}] ------", this.serverAddress, this.serverPort);
         ServerBootstrap bootstrap = new ServerBootstrap();
         bootstrap.group(this.bossGroup, this.workerGroup).channel(NioServerSocketChannel.class);
         bootstrap.childHandler(this.serverInitializer);
         //版本更新之后需要换成childOption
         bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

         try {
            bootstrap.bind(this.serverAddress, this.serverPort).sync();
            LOG.info("------ Convnet server listening on [{}:{}] ready to serve ------", this.serverAddress, this.serverPort);
            this.running = true;
         } catch (Throwable var3) {
            LOG.error("Server startup error", var3);
         }

      }
   }

   public void stop() {
      if (this.running) {
         this.bossGroup.shutdownGracefully();
         this.workerGroup.shutdownGracefully();
         LOG.info("---- Convnet server shutdown successfully ----", this.serverAddress, this.serverPort);
      }

   }

   public boolean isRunning() {
      return this.running;
   }

   public void destroy() throws Exception {
      this.stop();
   }

   public void afterPropertiesSet() throws Exception {
      this.bossGroup = new NioEventLoopGroup();
      this.workerGroup = new NioEventLoopGroup(this.poolSize);
      this.start();
   }
}

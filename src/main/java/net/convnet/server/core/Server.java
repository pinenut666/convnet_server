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

import java.lang.reflect.Constructor;

/**
 * ConVNet服务器核心启动类
 *
 * 该类实现了Lifecycle,Bean的初始化和销毁。
 * 其首先读取ServerInitialzer参数，并确认启动端口号，并运行启动。
 * @author Administrator
 * @date 2024/01/02
 */
public final class Server implements Lifecycle, InitializingBean, DisposableBean {
   private static final Logger LOG = LoggerFactory.getLogger(Server.class);

   /**
    *  EventLoopGroup 是一组 EventLoop 的集合，
    *  每个 EventLoop 负责处理一个或多个 Channel 的 I/O 操作，
    *  它们负责处理传入的事件并将其派发到注册的处理器（handlers）。
    *  此处放在外围的主要原因，是初始化时可以提前设置，销毁时可以调用销毁函数。
    */
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

   /**
    * 开始，重写了LifeCycle的start，组件运行时会执行下面的代码。
    */
   @Override
   public void start() {
      if (this.running) {
         LOG.info("Already running");
      } else {
         LOG.info("------ Attempt to start convnet server on [{}:{}] ------", this.serverAddress, this.serverPort);
         //Netty代码的初始化
         ServerBootstrap bootstrap = new ServerBootstrap();
         //加载已经在afterPropertiesSet初始化时设置了的两个EventLoop
         bootstrap.group(this.bossGroup, this.workerGroup).channel(NioServerSocketChannel.class);
         //加载对应的处理器集合
         bootstrap.childHandler(this.serverInitializer);
         //版本更新之后需要换成childOption，打开TCP_NODELAY，即Nagle算法
         bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
         try {
            //绑定对应接口并运行
            bootstrap.bind(this.serverAddress, this.serverPort).sync();
            LOG.info("------ Convnet server listening on [{}:{}] ready to serve ------", this.serverAddress, this.serverPort);
            this.running = true;
         } catch (Throwable var3) {
            LOG.error("Server startup error", var3);
         }

      }
   }

   @Override
   public void stop() {
      if (this.running) {
         this.bossGroup.shutdownGracefully();
         this.workerGroup.shutdownGracefully();
         LOG.info("---- Convnet server on [{}:{}] shutdown successfully ----", this.serverAddress, this.serverPort);
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
      this.bossGroup = new NioEventLoopGroup();
      this.workerGroup = new NioEventLoopGroup(this.poolSize);
      this.start();
   }
}

package net.convnet.server.core;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import net.convnet.server.protocol.Filter;
import net.convnet.server.protocol.FilterChain;
import net.convnet.server.protocol.Processor;
import net.convnet.server.protocol.ProtocolFactory;
import net.convnet.server.session.SessionManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 服务器初始值设定项
 *
 * @author Administrator
 * @date 2024/01/02
 */
public final class ServerInitializer extends ChannelInitializer<SocketChannel> implements ApplicationContextAware, InitializingBean, DisposableBean {
   private ApplicationContext appCtx;
   private ProtocolFactory protocolFactory;
   private SessionManager sessionManager;
   private final List<Filter> filters = new ArrayList<>();
   private ChannelHandler hibernateSessionHandle;
   private FilterChain chain;
   private ChannelHandler handshakeHandle;
   private ChannelHandler encoderHandle;
   @Value("${props.defaultUserLimitSend}")
   private int defaultUserLimitSend;

   public void setProtocolFactory(ProtocolFactory protocolFactory) {
      this.protocolFactory = protocolFactory;
   }

   public void setSessionManager(SessionManager sessionManager) {
      this.sessionManager = sessionManager;
   }

   public void setFilters(List<Filter> filters) {
      this.filters.addAll(filters);
   }


   /**
    * 初始化通道
    *
    * @param ch CH
    */
   @Override
   protected void initChannel(SocketChannel ch) {
      ChannelPipeline pipeline = ch.pipeline();
      //pipeline加载了各种管道，对应的管道如下
      // ReadTimeOutHandler 在多长时间后若没有任何消息发出时就会抛出异常。
      pipeline.addLast("readTimeOut", new ReadTimeoutHandler(600));
      //该HandShake管道在DEBUG模式下将会输出收发的消息。
      pipeline.addLast("handshake", this.handshakeHandle);
      //限速管道，该管道仅对write(写）的速率进行了限制，对读速率不做限制。
      pipeline.addLast("speedLimit", new ChannelTrafficShapingHandler(this.defaultUserLimitSend, 0L));
      //创建包解码器，使用会话 + 自定义包解码器
      pipeline.addLast("decoder", new PacketDecoder(this.sessionManager));
      //JPA 特殊
     // pipeline.addLast("hibernateSession", this.hibernateSessionHandle);
      pipeline.addLast("encoder", this.encoderHandle);
      //创建管道：ConVNet 来处理所有中逻辑
      pipeline.addLast("handler", new ConvnetHandler(this.protocolFactory, this.sessionManager, this.chain));
      //日志记录模块，使用原生NETTY
      pipeline.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
   }

   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.appCtx = applicationContext;
   }

   /**
    * 属性设置后
    *
    */
   @Override
   public void afterPropertiesSet() {
      this.filters.add(new ProcessFilter(this.appCtx.getBeansOfType(Processor.class).values()));

      for (Filter filter : this.filters) {
         filter.init();
      }

      this.chain = new DefaultFilterChain(this.filters);
      //创建自定义handshake
      this.handshakeHandle = new HandshakeHandle();
      this.encoderHandle = new PacketEncoder(this.protocolFactory);
   }

   @Override
   public void destroy() {
      for (Filter filter : this.filters) {
         filter.destroy();
      }

   }
}

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

public final class ServerInitializer extends ChannelInitializer<SocketChannel> implements ApplicationContextAware, InitializingBean, DisposableBean {
   private ApplicationContext appCtx;
   private ProtocolFactory protocolFactory;
   private SessionManager sessionManager;
   private final List<Filter> filters = new ArrayList();
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


   @Override
   protected void initChannel(SocketChannel ch) throws Exception {
      ChannelPipeline pipeline = ch.pipeline();
      pipeline.addLast("readTimeOut", new ReadTimeoutHandler(600));
      pipeline.addLast("handshake", this.handshakeHandle);
      pipeline.addLast("speedLimit", new ChannelTrafficShapingHandler((long)this.defaultUserLimitSend, 0L));
      pipeline.addLast("decoder", new PacketDecoder(this.sessionManager));
     // pipeline.addLast("hibernateSession", this.hibernateSessionHandle);
      pipeline.addLast("encoder", this.encoderHandle);
      pipeline.addLast("handler", new ConvnetHandler(this.protocolFactory, this.sessionManager, this.chain));
      pipeline.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
   }

   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.appCtx = applicationContext;
   }

   @Override
   public void afterPropertiesSet() throws Exception {
      this.filters.add(new ProcessFilter(this.appCtx.getBeansOfType(Processor.class).values()));

      for (Filter filter : this.filters) {
         filter.init();
      }

      this.chain = new DefaultFilterChain(this.filters);
      this.handshakeHandle = new HandshakeHandle();
      this.encoderHandle = new PacketEncoder(this.protocolFactory);
   }

   @Override
   public void destroy() throws Exception {
      for (Filter filter : this.filters) {
         filter.destroy();
      }

   }
}

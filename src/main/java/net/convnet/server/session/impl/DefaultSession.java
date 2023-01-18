package net.convnet.server.session.impl;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.convnet.server.Constants;
import net.convnet.server.session.Session;
import net.convnet.server.support.attr.JSONObjectAttrable;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

abstract class DefaultSession extends JSONObjectAttrable implements Session {
   private static final long serialVersionUID = 6278446630616735565L;
   static AttributeKey<Session> ATTR_KEY = AttributeKey.newInstance("CONVNET_SESSION");
   private static final AtomicLong COUNTER = new AtomicLong();
   private final long id;
   private final int userId;
   private final Channel channel;
   private String ip;
   private Integer port;
   private String mac;
   private int protocolVersion;
   private boolean closed;

   public DefaultSession(int userId, Channel channel) {
      this.userId = userId;
      this.channel = channel;
      this.id = COUNTER.incrementAndGet();
      this.channel.attr(ATTR_KEY).set(this);
   }

   public long getId() {
      return this.id;
   }

   public int getUserId() {
      return this.userId;
   }

   public Channel getChannel() {
      return this.channel;
   }

   public String getIp() {
      if (this.ip == null) {
         this.fillAddress();
      }

      return this.ip;
   }

   public int getPort() {
      if (this.port == null) {
         this.fillAddress();
      }

      return this.port;
   }

   private void fillAddress() {
      InetSocketAddress address = (InetSocketAddress)this.channel.remoteAddress();
      this.ip = address.getAddress().getHostAddress();
      this.port = address.getPort();
   }

   public String getMac() {
      return this.getAttr("mac");
   }

   public void setMac(String mac) {
      this.mac = mac;
   }

   public int getProtocolVersion() {
      return this.protocolVersion;
   }

   public void setProtocolVersion(int protocolVersion) {
      this.protocolVersion = protocolVersion;
   }

   public boolean isClosed() {
      return this.closed;
   }

   public void setClosed(boolean closed) {
      this.closed = closed;
   }

   public void destory() {
      this.channel.attr(ATTR_KEY).remove();
   }

   public long getWriteBytes() {
      return this.def((Long)this.channel.attr(Constants.WRITE_BYTES_KEY).get(), 0L);
   }

   public long getReadBytes() {
      return this.def((Long)this.channel.attr(Constants.READ_BYTES_KEY).get(), 0L);
   }

   private long def(Long l, long def) {
      return l == null ? def : l;
   }
}

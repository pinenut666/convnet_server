package net.convnet.server.session;

import io.netty.channel.Channel;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.support.attr.Attrable;

public interface Session extends Attrable {
   long getId();

   boolean isLogin();

   int getUserId();

   User getUser();

   String getIp();

   int getPort();

   String getMac();

   Channel getChannel();

   int getProtocolVersion();

   void destory();

   long getReadBytes();

   long getWriteBytes();
}

package net.convnet.server.protocol.bin.coder;

import cn.hutool.extra.spring.SpringUtil;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.ResponseReader;
import net.convnet.server.protocol.bin.AbstractPacketCoder;
import net.convnet.server.protocol.bin.BinaryPacket;
import net.convnet.server.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class GetFriendInfoCoder extends AbstractPacketCoder {
   private SessionManager sessionManager;

   //使用方法的返回值获取对象而不是使用成员变量直接注入

   public SessionManager getSessionManager() {
      return SpringUtil.getBean("sessionManager",SessionManager.class);
   }

   @Override
   public Cmd getCmd() {
      return Cmd.GET_FRIEND_INFO;
   }

   @Override
   public Cmd getRespCmd() {
      return Cmd.GET_FRIEND_INFO_RESP;
   }

   @Override
   public void encode(ResponseReader reader, BinaryPacket packet) {
      List<User> friends = reader.getAttr("friends");

      for (User user : friends) {
         packet.add(user.getId());
         packet.add(user.getNickName());
         packet.add(this.getSessionManager().getSession(user.getId()) != null);
      }

      super.encode(reader, packet);
   }
}

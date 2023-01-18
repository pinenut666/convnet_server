package net.convnet.server.protocol.bin.coder;

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
   @Autowired
   private SessionManager sessionManager;

   public Cmd getCmd() {
      return Cmd.GET_FRIEND_INFO;
   }

   public Cmd getRespCmd() {
      return Cmd.GET_FRIEND_INFO_RESP;
   }

   public void encode(ResponseReader reader, BinaryPacket packet) {
      List<User> friends = (List)reader.getAttr("friends");
      Iterator i$ = friends.iterator();

      while(i$.hasNext()) {
         User user = (User)i$.next();
         packet.add(user.getId());
         packet.add(user.getNickName());
         packet.add(this.sessionManager.getSession(user.getId()) != null);
      }

      super.encode(reader, packet);
   }
}

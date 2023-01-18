package net.convnet.server.protocol.bin.coder;

import net.convnet.server.identity.GroupManager;
import net.convnet.server.identity.UserManager;
import net.convnet.server.mybatis.pojo.Group;
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
public class GetGroupInfoCoder extends AbstractPacketCoder {
   @Autowired
   private SessionManager sessionManager;
   @Autowired
   private UserManager userManager;

   public Cmd getCmd() {
      return Cmd.GET_GROUP_INFO;
   }

   public Cmd getRespCmd() {
      return Cmd.GET_GROUP_INFO_RESP;
   }

   public void encode(ResponseReader reader, BinaryPacket packet) {
      List<Group> groups = (List)reader.getAttr("groups");
      Iterator i$ = groups.iterator();

      while(i$.hasNext()) {
         Group group = (Group)i$.next();
         this.addGroup(packet, group);
      }

      super.encode(reader, packet);
   }

   private void addGroup(BinaryPacket packet, Group group) {
      packet.add("G");
      packet.add(group.getName());
      packet.add(group.getId());
      packet.add(group.getCreatorId());
      Iterator i$ = userManager.getUserByGroupId(group.getId()).iterator();

      while(i$.hasNext()) {
         User user = (User)i$.next();
         this.addUser(packet, user);
      }

   }

   private void addUser(BinaryPacket packet, User user) {
      packet.add("U");
      packet.add(user.getNickName());
      packet.add(user.getId());
      packet.add(this.sessionManager.getSession(user.getId()) != null);
   }
}

package net.convnet.server.protocol.bin.coder;

import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.ResponseReader;
import net.convnet.server.protocol.bin.AbstractPacketCoder;
import net.convnet.server.protocol.bin.BinaryPacket;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class FindUserCoder extends AbstractPacketCoder {
   public Cmd getCmd() {
      return Cmd.FIND_USER;
   }

   public Cmd getRespCmd() {
      return Cmd.FIND_USER_RESP;
   }

   public void decode(RequestBuilder builder, BinaryPacket packet) {
      builder.set("type", packet.get(0));
      builder.set("value", packet.get(1));
   }

   public void encode(ResponseReader reader, BinaryPacket packet) {
      List<User> users = reader.getAttr("users");
      if (users == null) {
         packet.add("N");
      } else {
         packet.add(users.size());

         for (User user : users) {
            packet.add(user.getId());
            packet.add(user.getName());
            packet.add(user.getNickName());
         }

         super.encode(reader, packet);
      }
   }
}

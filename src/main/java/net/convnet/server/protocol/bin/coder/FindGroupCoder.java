package net.convnet.server.protocol.bin.coder;

import net.convnet.server.mybatis.pojo.Group;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.ResponseReader;
import net.convnet.server.protocol.bin.AbstractPacketCoder;
import net.convnet.server.protocol.bin.BinaryPacket;
import net.convnet.server.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class FindGroupCoder extends AbstractPacketCoder {

   @Override
   public Cmd getCmd() {
      return Cmd.FIND_GROUP;
   }

   @Override
   public Cmd getRespCmd() {
      return Cmd.FIND_GROUP_RESP;
   }

   @Override
   public void decode(RequestBuilder builder, BinaryPacket packet) {
      builder.set("type", packet.get(0));
      builder.set("value", packet.get(1));
   }

   @Override
   public void encode(ResponseReader reader, BinaryPacket packet) {
      List<Group> groups = reader.getAttr("groups");
      if (groups == null) {
         packet.add("N");
      } else {
         packet.add(groups.size());

         for (Group group : groups) {
            packet.add(group.getId());
            packet.add(group.getName());
            packet.add(group.getDescription());
         }

         super.encode(reader, packet);
      }
   }
}

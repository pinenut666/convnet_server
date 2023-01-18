package net.convnet.server.protocol.bin.coder;

import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.bin.AbstractPacketCoder;
import net.convnet.server.protocol.bin.BinaryPacket;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateInfoCoder extends AbstractPacketCoder {
   public Cmd getCmd() {
      return Cmd.RENEW_MY_INFO;
   }

   public Cmd getRespCmd() {
      return Cmd.RENEW_MY_INFO_RESP;
   }

   public void decode(RequestBuilder builder, BinaryPacket packet) {
      if (packet.getParts().size() < 5) {
         builder.set("nickName", packet.get(0));
         builder.set("password", packet.get(1));
         builder.set("description", packet.get(2));
      } else {
         builder.set("nickName", packet.get(0));
         builder.set("password", packet.get(1));
         builder.set("description", packet.get(2));
         builder.set("p1", packet.get(3));
         builder.set("p2", packet.get(4));
         builder.set("p3", packet.get(5));
         builder.set("p4", packet.get(6));
      }

   }
}

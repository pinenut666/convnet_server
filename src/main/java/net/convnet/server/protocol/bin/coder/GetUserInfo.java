package net.convnet.server.protocol.bin.coder;

import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.ResponseReader;
import net.convnet.server.protocol.bin.AbstractPacketCoder;
import net.convnet.server.protocol.bin.BinaryPacket;
import org.springframework.stereotype.Service;

@Service
public class GetUserInfo extends AbstractPacketCoder {
   public Cmd getCmd() {
      return Cmd.GET_USERINFO;
   }

   public Cmd getRespCmd() {
      return Cmd.GET_USERINFO_RESP;
   }

   public void decode(RequestBuilder builder, BinaryPacket packet) {
      builder.set("userid", packet.get(0));
   }

   public void encode(ResponseReader reader, BinaryPacket packet) {
      packet.add("*" + reader.getAttr("userdesc"));
      packet.NeedEndStar(false);
   }
}

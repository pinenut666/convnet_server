package net.convnet.server.protocol.bin.coder;

import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.ResponseReader;
import net.convnet.server.protocol.bin.AbstractPacketCoder;
import net.convnet.server.protocol.bin.BinaryPacket;
import net.convnet.server.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMsgCoder extends AbstractPacketCoder {
   @Autowired
   private SessionManager sessionManager;

   public Cmd getCmd() {
      return Cmd.SEND_MSGTO_ID;
   }

   public Cmd getRespCmd() {
      return Cmd.SEND_MSGTO_ID_RESP;
   }

   public void decode(RequestBuilder builder, BinaryPacket packet) {
      builder.set("userid", packet.get(0));
      builder.set("msg", packet.get(1));
   }

   public void encode(ResponseReader reader, BinaryPacket packet) {
      packet.add(reader.getAttr("userid"));
      packet.add("*" + reader.getAttr("msg"));
      packet.NeedEndStar(false);
      super.encode(reader, packet);
   }
}

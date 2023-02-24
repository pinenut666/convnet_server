package net.convnet.server.protocol.bin.coder;

import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.ResponseReader;
import net.convnet.server.protocol.bin.AbstractPacketCoder;
import net.convnet.server.protocol.bin.BinaryPacket;
import net.convnet.server.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMsgNotArriveCoder extends AbstractPacketCoder {

   @Override
   public Cmd getCmd() {
      return Cmd.NULLPACK;
   }

   @Override
   public Cmd getRespCmd() {
      return Cmd.MSG_CAN_TARRIVE;
   }

   @Override
   public void encode(ResponseReader reader, BinaryPacket packet) {
      packet.add(reader.getAttr("userid"));
      packet.add("*" + reader.getAttr("msg"));
      packet.NeedEndStar(false);
      super.encode(reader, packet);
   }
}

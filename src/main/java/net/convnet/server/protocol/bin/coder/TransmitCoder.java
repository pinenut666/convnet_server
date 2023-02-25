package net.convnet.server.protocol.bin.coder;

import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.bin.AbstractPacketCoder;
import net.convnet.server.protocol.bin.BinaryPacket;
import org.springframework.stereotype.Service;

@Service
public class TransmitCoder extends AbstractPacketCoder {
   @Override
   public Cmd getCmd() {
      return Cmd.SERVER_TRANS;
   }

   @Override
   public void decode(RequestBuilder builder, BinaryPacket packet) {
      builder.set("userId", packet.get(0));
      builder.set("payload", packet.get(1));
   }
}

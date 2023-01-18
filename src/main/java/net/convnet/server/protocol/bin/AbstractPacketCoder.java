package net.convnet.server.protocol.bin;

import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.ResponseReader;

public abstract class AbstractPacketCoder implements PacketCoder {
   public Cmd getRespCmd() {
      return this.getCmd();
   }

   public void decode(RequestBuilder builder, BinaryPacket packet) {
   }

   public void encode(ResponseReader reader, BinaryPacket packet) {
      if (packet.IsNeedStar()) {
         packet.end();
      }

   }
}

package net.convnet.server.protocol.bin;

import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.ResponseReader;

public interface PacketCoder {
   Cmd getCmd();

   Cmd getRespCmd();

   void decode(RequestBuilder var1, BinaryPacket var2);

   void encode(ResponseReader var1, BinaryPacket var2);
}

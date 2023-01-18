package net.convnet.server.protocol;

import io.netty.buffer.ByteBuf;

public interface Protocol {
   int getVersion();

   String getVersionCode();

   Request decode(ByteBuf var1);

   void encode(ResponseReader var1, ByteBuf var2);

   Response createResponse(Cmd var1);

   Response exToResponse(Throwable var1);
}

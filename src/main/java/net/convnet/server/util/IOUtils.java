package net.convnet.server.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.convnet.server.Constants;

public final class IOUtils {
   public static void writeString(ByteBuf buff, String str) {
      buff.writeBytes(toByteBuf(str));
   }

   public static ByteBuf toByteBuf(String str) {
      return Unpooled.copiedBuffer(str, Constants.CHARSET);
   }

   private IOUtils() {
   }
}

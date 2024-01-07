package net.convnet.server.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.convnet.server.Constants;

/**
 * 流的辅助处理类
 *
 * @author Administrator
 * @date 2024/01/07
 */
public final class IOUtils {
   /**
    * 将传输的字符串写入传入的byteBuf中
    *
    * @param buff 迷
    * @param str  str
    */
   public static void writeString(ByteBuf buff, String str) {
      //其本质是把字符串转为byteBuf，之后调用写入字节的方式写入
      buff.writeBytes(toByteBuf(str));
   }

   /**
    * 将字符串转换为ByteBuf
    *
    * @param str str
    * @return {@link ByteBuf}
    */
   public static ByteBuf toByteBuf(String str) {
      //使用Netty的方法将String复制到ByteBuf中
      return Unpooled.copiedBuffer(str, Constants.CHARSET);

   }
   private IOUtils() {
   }
}

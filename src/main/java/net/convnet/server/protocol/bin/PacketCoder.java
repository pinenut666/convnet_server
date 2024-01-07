package net.convnet.server.protocol.bin;

import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.ResponseReader;

/**
 * 数据包编码器
 *
 * @author Administrator
 * @date 2024/01/07
 */
public interface PacketCoder {
   /**
    * 获取发送的命令对象
    *
    * @return {@link Cmd}
    */
   Cmd getCmd();

   /**
    * 获取返回的命令对象
    *
    * @return {@link Cmd}
    */
   Cmd getRespCmd();

   /**
    * 解码
    *
    * @param var1 变量1
    * @param var2 变量2
    */
   void decode(RequestBuilder var1, BinaryPacket var2);

   /**
    * 编码
    *
    * @param var1 变量1
    * @param var2 变量2
    */
   void encode(ResponseReader var1, BinaryPacket var2);
}

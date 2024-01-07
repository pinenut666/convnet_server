package net.convnet.server.protocol.bin;

import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.ResponseReader;

/**
 * 数据包编码器抽象类
 * 每个Coder都会扩写它。
 * @author Administrator
 * @date 2024/01/07
 */
public abstract class AbstractPacketCoder implements PacketCoder {
   @Override
   public Cmd getRespCmd() {
      return this.getCmd();
   }

   @Override
   public void decode(RequestBuilder builder, BinaryPacket packet) {
   }

   @Override
   public void encode(ResponseReader reader, BinaryPacket packet) {
      if (packet.IsNeedStar()) {
         packet.end();
      }

   }
}

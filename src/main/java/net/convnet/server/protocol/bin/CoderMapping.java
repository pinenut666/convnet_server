package net.convnet.server.protocol.bin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.ResponseReader;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 编码器映射
 *
 * @author Administrator
 * @date 2024/01/07
 */
@ToString
@Data
public final class CoderMapping implements PacketCoder {
   private Cmd cmd;
   private Cmd rCmd;
   private String[] encode;
   private String[] decode;
   private String star;

   /**
    * 编码器映射
    */
   public CoderMapping() {
      this.encode = ArrayUtils.EMPTY_STRING_ARRAY;
      this.decode = ArrayUtils.EMPTY_STRING_ARRAY;
   }

   /**
    * 编码器映射
    *
    * @param cmd    命令名
    * @param rCmd   返回命令名
    * @param encode 编码
    * @param decode 解码
    * @param star   星
    */
   public CoderMapping(Cmd cmd, Cmd rCmd, String[] encode, String[] decode, String star) {
      this.cmd = cmd;
      this.rCmd = rCmd;
      this.encode = encode;
      this.decode = decode;
      this.star = star;
      if(encode==null){
         this.encode = ArrayUtils.EMPTY_STRING_ARRAY;
      }
      if(decode==null){
         this.decode = ArrayUtils.EMPTY_STRING_ARRAY;
      }
   }


   /**
    * 获取 CMD
    *
    * @return {@link Cmd}
    */
   @Override
   public Cmd getCmd() {
      return this.cmd;
   }

   /**
    * 获取 RESP CMD
    *
    * @return {@link Cmd}
    */
   @Override
   public Cmd getRespCmd() {
      return this.rCmd == null ? this.cmd : this.rCmd;
   }

   /**
    * 解码
    *
    * @param builder 建筑工人
    * @param packet  包
    */
   @Override
   public void decode(RequestBuilder builder, BinaryPacket packet) {
      int i = 0;

      for(int len = this.decode.length; i < len; ++i) {
         builder.set(this.decode[i], packet.get(i));
      }

   }

   /**
    * 编码
    *
    * @param reader 读者
    * @param packet 包
    */
   @Override
   public void encode(ResponseReader reader, BinaryPacket packet) {
      if (this.encode.length == 0 && this.star == null) {
         reader.setOutput(false);
      } else {
         for (String name : encode) {
            packet.add(reader.getAttr(name));
         }
         if (this.star != null) {
            packet.end(reader.getAttr(this.star));
         } else if (packet.IsNeedStar()) {
            packet.end();
         }

      }
   }
}

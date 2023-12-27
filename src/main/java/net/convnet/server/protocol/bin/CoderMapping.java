package net.convnet.server.protocol.bin;

import lombok.AllArgsConstructor;
import lombok.ToString;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.RequestBuilder;
import net.convnet.server.protocol.ResponseReader;
import org.apache.commons.lang3.ArrayUtils;
@ToString
public final class CoderMapping implements PacketCoder {
   private Cmd cmd;
   private Cmd rCmd;
   private String[] encode;
   private String[] decode;
   private String star;

   public CoderMapping() {
      this.encode = ArrayUtils.EMPTY_STRING_ARRAY;
      this.decode = ArrayUtils.EMPTY_STRING_ARRAY;
   }

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

   public void setCmd(Cmd cmd) {
      this.cmd = cmd;
   }

   public void setrCmd(Cmd rCmd) {
      this.rCmd = rCmd;
   }

   public void setStar(String star) {
      this.star = star;
   }

   public void setEncode(String[] encode) {
      this.encode = encode;
   }

   public void setDecode(String[] decode) {
      this.decode = decode;
   }

   @Override
   public Cmd getCmd() {
      return this.cmd;
   }

   @Override
   public Cmd getRespCmd() {
      return this.rCmd == null ? this.cmd : this.rCmd;
   }

   @Override
   public void decode(RequestBuilder builder, BinaryPacket packet) {
      int i = 0;

      for(int len = this.decode.length; i < len; ++i) {
         builder.set(this.decode[i], packet.get(i));
      }

   }

   @Override
   public void encode(ResponseReader reader, BinaryPacket packet) {
      if (this.encode.length == 0 && this.star == null) {
         reader.setOutput(false);
      } else {
         String[] arr$ = this.encode;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String name = arr$[i$];
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

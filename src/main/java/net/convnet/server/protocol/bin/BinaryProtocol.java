package net.convnet.server.protocol.bin;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;
import net.convnet.server.Constants;
import net.convnet.server.ex.CodecException;
import net.convnet.server.ex.ConvnetException;
import net.convnet.server.ex.ExceptionUtils;
import net.convnet.server.protocol.*;
import net.convnet.server.util.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class BinaryProtocol implements Protocol, ApplicationContextAware, InitializingBean {
   public static final int VERSION = 1;
   public static final String VERSION_CODE = "4.2";
   public static final char SEPARATOR = ',';
   public static final char STAR = '*';
   private Map<Cmd, PacketCoder> packetCoders = new HashMap<>();
   private List<CoderMapping> coderMappings = Collections.emptyList();
   private ApplicationContext appCtx;

   public void setCoderMappings(List<CoderMapping> coderMappings) {
      this.coderMappings = coderMappings;
   }

   @Override
   public int getVersion() {
      return 1;
   }

   @Override
   public String getVersionCode() {
      return "4.2";
   }

   @Override
   public Request decode(ByteBuf buff) {
      BinaryPacket packet = this.parseBinaryPacket(buff);
      DefaultRequest request = new DefaultRequest(packet.getCmd(), this.getVersion());
      this.getCoder(packet.getCmd()).decode(request, packet);
      return request;
   }

   private BinaryPacket parseBinaryPacket(ByteBuf buff) {
      byte c = buff.getByte(0);
      String[] dataArr;
      if (c == 48) {
         int index = buff.forEachByte(new ByteProcessor() {
            @Override
            public boolean process(byte value) throws Exception {
               return value != 42;
            }
         });
         dataArr = StringUtils.splitPreserveAllTokens(buff.toString(2, index - 2, Constants.CHARSET), ',');
         return new BinaryPacket(Cmd.SERVER_TRANS, new Object[]{dataArr[1], buff});
      } else {
         String body = buff.toString(Constants.CHARSET);
         int starIndex = body.indexOf(42);
         Cmd cmd;
         String[] arr;
         if (starIndex > -1) {
            arr = StringUtils.splitPreserveAllTokens(body.substring(0, starIndex), ',');
            dataArr = new String[arr.length];
            System.arraycopy(arr, 1, dataArr, 0, dataArr.length - 1);
            dataArr[dataArr.length - 1] = body.substring(starIndex + 1);
            cmd = this.toCmd(arr[0]);
         } else {
            arr = StringUtils.splitPreserveAllTokens(body, ',');
            dataArr = new String[arr.length - 1];
            System.arraycopy(arr, 1, dataArr, 0, dataArr.length);
            cmd = this.toCmd(arr[0]);
         }

         return new BinaryPacket(cmd, dataArr);
      }
   }

   @Override
   public void encode(ResponseReader reader, ByteBuf buff) {
      if (reader.needOutput()) {
         Cmd cmd = reader.getCmd();
         if (cmd == Cmd.SERVER_TRANS) {
            buff.writeBytes((ByteBuf)reader.getAttr("payload"));
         } else {
            BinaryPacket packet = new BinaryPacket();
            if (reader.isSuccess() || cmd == Cmd.ERROR) {
               this.getCoder(cmd).encode(reader, packet);
            }

            StringBuilder sb = new StringBuilder(32);
            sb.append(cmd.toOrdinal());

            for (Object part : packet.getParts()) {
               sb.append(',');
               if (part instanceof Boolean) {
                  sb.append((Boolean) part ? 'T' : 'F');
               } else if (part != null) {
                  sb.append(part);
               }
            }

            IOUtils.writeString(buff, sb.toString());
         }
      }
   }

   @Override
   public Response createResponse(Cmd cmd) {
      return new DefaultResponse(this.getVersion(), this.getCoder(cmd).getRespCmd());
   }

   @Override
   public Response exToResponse(Throwable e) {
      Response response = this.createResponse(Cmd.ERROR);
      if (e instanceof InvocationTargetException) {
         e = ((InvocationTargetException)e).getTargetException();
      }

      int code = 2;
      if (e instanceof ConvnetException) {
         code = ((ConvnetException)e).getCode();
      } else if (e instanceof IllegalArgumentException) {
         code = 2;
      } else if (e instanceof IllegalStateException) {
         code = 4;
      } else if (e instanceof UnsupportedOperationException) {
         code = 5;
      } else if (e instanceof DataAccessException) {
         code = 50;
      }

      response.setAttr("code", code);
      response.setAttr("msg", ExceptionUtils.buildMessage(e));
      return response;
   }

   private PacketCoder getCoder(Cmd cmd) {
      //这里的指令初始化并没有我们的实现类
      PacketCoder coder = (PacketCoder)this.packetCoders.get(cmd);
      if (coder == null) {
         throw new CodecException("PacketCoder for cmd [" + cmd + "] not found");
      } else {
         return coder;
      }
   }

   private Cmd toCmd(String s) {
      try {
         return Cmd.values()[Integer.parseInt(s)];
      } catch (NumberFormatException var3) {
         throw new CodecException("Invalid cmd [" + s + "]");
      }
   }

   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.appCtx = applicationContext;
   }

   @Override
   public void afterPropertiesSet() throws Exception {
      //加载实现类的
      Collection<PacketCoder> pack = appCtx.getBeansOfType(PacketCoder.class).values();
      String[] a = appCtx.getBeanNamesForType(PacketCoder.class);

      for(String beanname:a)
      {
         PacketCoder coder = (PacketCoder) appCtx.getBean(beanname);
         this.packetCoders.put(coder.getCmd(), coder);
         this.packetCoders.put(coder.getRespCmd(), coder);
      }

/*      for(PacketCoder coder:appCtx.getBeansOfType(PacketCoder.class).values()) {
         this.packetCoders.put(coder.getCmd(), coder);
         this.packetCoders.put(coder.getRespCmd(), coder);
      }*/

      for(CoderMapping mapping:this.coderMappings) {
         this.packetCoders.put(mapping.getCmd(), mapping);
         this.packetCoders.put(mapping.getRespCmd(), mapping);
      }

   }
}

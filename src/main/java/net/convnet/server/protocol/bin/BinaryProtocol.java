package net.convnet.server.protocol.bin;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;
import lombok.ToString;
import net.convnet.server.Constants;
import net.convnet.server.ex.CodecException;
import net.convnet.server.ex.ConvnetException;
import net.convnet.server.ex.ExceptionUtils;
import net.convnet.server.protocol.*;
import net.convnet.server.util.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 二进制协议流转换
 *
 * @author yuyuhaso
 * @date 2024/01/07
 */
public final class BinaryProtocol implements Protocol, ApplicationContextAware, InitializingBean {
   private static final Logger LOG = LoggerFactory.getLogger(BinaryProtocol.class);
   //默认版本号为1
   public static final int VERSION = 1;
   //版本号代码为4.2
   public static final String VERSION_CODE = "4.2";
   //以逗号分割
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
      return VERSION;
   }

   @Override
   public String getVersionCode() {
      return VERSION_CODE;
   }

   /**
    * 解码方法
    *
    * @param buff ByteBuf参数
    * @return {@link Request}
    */
   @Override
   public Request decode(ByteBuf buff) {
      //将对应的ByteBuf首先转换为二进制包
      BinaryPacket packet = this.parseBinaryPacket(buff);
      //以包对应的指令和版本，构建一个请求对象
      DefaultRequest request = new DefaultRequest(packet.getCmd(), this.getVersion());
      //输出DEBUG日志
      LOG.debug("Request send is" + request);
      this.getCoder(packet.getCmd()).decode(request, packet);
      return request;
   }

   /**
    * 解析二进制数据包
    *
    * @param buff ByteBuf
    * @return {@link BinaryPacket}
    */
   private BinaryPacket parseBinaryPacket(ByteBuf buff) {
      //首先获取第1个字节
      byte c = buff.getByte(0);
      //创建字符数组
      String[] dataArr;
      //如果字节是48（即0）
      if (c == 48) {
         //遍历每一个字节，直到这个字节是42（对应ASCII是*）
         int index = buff.forEachByte(new ByteProcessor() {
            @Override
            public boolean process(byte value) throws Exception {
               return value != 42;
            }
         });
         //此时，从2号位开始，以逗号为分割向后切分
         dataArr = StringUtils.splitPreserveAllTokens(
                 buff.toString(2, index - 2, Constants.CHARSET)
                 , ',');
         //TODO：推演逻辑
         //并返回一个新的Packet对象，属性为SERVER_TRANS，填入对应的bytebuf和dataArr[1]
         return new BinaryPacket(Cmd.SERVER_TRANS, new Object[]{dataArr[1], buff});
      } else {
         //否则（即不是0的情况）
         //将对应的body转为string
         String body = buff.toString(Constants.CHARSET);
         //寻找星号的位置
         int starIndex = body.indexOf(42);
         Cmd cmd;
         String[] arr;
         //TODO:推演逻辑
         if (starIndex > -1) {
            //如果找到了星号
            //切分星号之后，以都好分割
            arr = StringUtils.splitPreserveAllTokens(body.substring(0, starIndex), ',');
            //创建了一个与给定数组长度相同的新字符串数组 dataArr。
            dataArr = new String[arr.length];
            //将给定数组 arr 除了第一个元素外的其他元素复制到 dataArr 中。
            System.arraycopy(arr, 1, dataArr, 0, dataArr.length - 1);
            //将 body 字符串中 starIndex + 1 开始的子字符串赋值给 dataArr 数组的最后一个元素。
            dataArr[dataArr.length - 1] = body.substring(starIndex + 1);
         } else {
            //没有找到星号的情况，直接全部复制
            arr = StringUtils.splitPreserveAllTokens(body, ',');
            dataArr = new String[arr.length - 1];
            System.arraycopy(arr, 1, dataArr, 0, dataArr.length);
         }
         //将arr[0]转换为Cmd
         cmd = this.toCmd(arr[0]);
         //并返回对应的包
         return new BinaryPacket(cmd, dataArr);
      }
   }

   /**
    * 编码方法
    *
    * @param reader 读者
    * @param buff   迷
    */
   @Override
   public void encode(ResponseReader reader, ByteBuf buff) {
      //输出当前的Response
      LOG.debug("Response send is" + reader);
      //如果ResponseReader的needOutput为true
      if (reader.needOutput()) {
         //获取对应指令
         Cmd cmd = reader.getCmd();
         //如果指令是服务器传输指令
         if (cmd == Cmd.SERVER_TRANS) {
            //将对应指令的负载直接输出到bytebuf中
            buff.writeBytes((ByteBuf)reader.getAttr("payload"));
         } else {
            //否则，创建一个新的二进制包
            BinaryPacket packet = new BinaryPacket();
            //如果Response是成功，或者指令是错误
            if (reader.isSuccess() || cmd == Cmd.ERROR) {
               //使用对应的Coder进行处理（这看来是发送给客户端的设计）
               this.getCoder(cmd).encode(reader, packet);
            }
            //创建一个容量为32的StringBuilder
            StringBuilder sb = new StringBuilder(32);
            //输入cmd的序数
            sb.append(cmd.toOrdinal());
            // 此时已经经过Coder处理后的packet已经被分成数块
            for (Object part : packet.getParts()) {
               //在分块的间隔中添加一个逗号
               sb.append(SEPARATOR);
               //如果发现这部分是布尔值，用T或者F代替
               if (part instanceof Boolean) {
                  sb.append((Boolean) part ? 'T' : 'F');
               } else if (part != null) {
                  //又或者是空的话，直接将它塞在后面（即可能出现两三个逗号的情况）
                  sb.append(part);
               }
            }
            //传输对应的字符串
            IOUtils.writeString(buff, sb.toString());
         }
      }
   }

   /**
    * 创建响应对象
    * 使用默认创建方式，创建一个对象。
    * @param cmd 需要返回的指令（它将会根据指令寻找对应的回应版本指令）
    * @return {@link Response}
    */
   @Override
   public Response createResponse(Cmd cmd) {
      return new DefaultResponse(this.getVersion(), this.getCoder(cmd).getRespCmd());
   }

   /**
    * 将错误信息转换为响应对象
    *
    * @param e e
    * @return {@link Response}
    */
   @Override
   public Response exToResponse(Throwable e) {
      //首先创建一个对应的错误回应
      Response response = this.createResponse(Cmd.ERROR);
      //如果它是调用对象异常
      //InvocationTargetException 可能会包裹其他异常
      // 比如在使用反射调用方法时，如果被调用方法内部抛出了异常
      // 这个异常会被 InvocationTargetException 包裹起来。
      // 因此，使用 getTargetException() 方法可以获取原始的被包裹的异常，便于后续处理。
      if (e instanceof InvocationTargetException) {
         e = ((InvocationTargetException)e).getTargetException();
      }
      //默认为2
      int code = 2;
      //如果是CVN规定的异常
      if (e instanceof ConvnetException) {
         //转换为CVN定义的异常代码
         code = ((ConvnetException)e).getCode();
      } else if (e instanceof IllegalArgumentException) {
         //非法参数为2
         code = 2;
      } else if (e instanceof IllegalStateException) {
         //启动子级时出错报4
         code = 4;
      } else if (e instanceof UnsupportedOperationException) {
         //不支持的操作为5
         code = 5;
      } else if (e instanceof DataAccessException) {
         //数据访问异常50
         code = 50;
      }
      //TODO:删掉对应，换成hutool以简化输出信息
      //设置对应的属性，和对应的报错信息（使用了Spring的NLS)
      response.setAttr("code", code);
      response.setAttr("msg", ExceptionUtils.buildMessage(e));
      return response;
   }

   /**
    * 通过命令获取对应的编码器
    *
    * @param cmd CMD
    * @return {@link PacketCoder}
    */
   private PacketCoder getCoder(Cmd cmd) {
      PacketCoder coder = this.packetCoders.get(cmd);
      if (coder == null) {
         throw new CodecException("PacketCoder for cmd [" + cmd + "] not found");
      } else {
         return coder;
      }
   }

   /**
    * 将对应的字符串转换为 cmd（字符串对应的是其枚举对应的数字）
    *
    * @param s s
    * @return {@link Cmd}
    */
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
      //装载Coder和Mapping
      for(PacketCoder coder:appCtx.getBeansOfType(PacketCoder.class).values()) {
         this.packetCoders.put(coder.getCmd(), coder);
         this.packetCoders.put(coder.getRespCmd(), coder);
      }

      for(CoderMapping mapping:this.coderMappings) {
         this.packetCoders.put(mapping.getCmd(), mapping);
         this.packetCoders.put(mapping.getRespCmd(), mapping);
      }

   }
}

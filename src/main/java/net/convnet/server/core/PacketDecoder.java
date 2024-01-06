package net.convnet.server.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import net.convnet.server.Constants;
import net.convnet.server.protocol.Protocol;
import net.convnet.server.protocol.Request;
import net.convnet.server.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CVN自定义数据包解码器
 * 其基于自定义帧长度解码器，该解码器较为复杂。
 *
 * @author Administrator
 * @date 2024/01/02
 */
final class PacketDecoder extends LengthFieldBasedFrameDecoder {
    private static final Logger LOG = LoggerFactory.getLogger(PacketDecoder.class);
    private SessionManager sessionManager;

    PacketDecoder(SessionManager sessionManager) {
        //构造发送数据包最长为51200字节，长度域偏移量为0，长度域字节数为4，长度域偏移量矫正为0，丢弃的起始字节数为4。
        //也就是说，一个包前面四个字节是长度域，后面是发送的数据，这也就解释了DEBUG模式下输出前面解析的是乱码的情况。
        super(51200, 0, 4, 0, 4);
        //创建SessionManager
        this.sessionManager = sessionManager;
    }

    /**
     * 自定义解码方法
     *
     * @param ctx CTX公司
     * @param in  在
     * @return {@link Object}
     * @throws Exception 例外
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame;
        try {
            //首先以自定义帧长度解码器对传来的流进行解码
            frame = (ByteBuf) super.decode(ctx, in);
        } catch (Exception var5) {
            LOG.info("Frame decode error", var5);
            ctx.channel().close();
            return null;
        }
        // TODO：确认READ_BYTES_KEY的部署方式
        // 获取该参数
        Long readBytes = ctx.channel().attr(Constants.READ_BYTES_KEY).get();
        // 若未能获得参数，则赋默认值0.
        if (readBytes == null) {
            readBytes = 0L;
        }
        // 如果流解码后为空，则返回空
        if (frame == null) {
            return null;
        } else {
            // 否则设置当前可读字节数
            ctx.channel().attr(Constants.READ_BYTES_KEY).set(readBytes + (long) frame.readableBytes());
            // 若是DEBUG模式，则打印日志
            if (LOG.isDebugEnabled()) {
                LOG.debug("Receive packet [" + frame.toString(Constants.CHARSET) + "]");
            }
            // 调用sessionManager对象的getProtocol方法，获取当前通道对应的协议对象，并使用该对象将ByteBuf对象解码为具体的响应对象
            Protocol protocol = this.sessionManager.getProtocol(ctx.channel());
            Request request = protocol.decode(frame);
            LOG.debug("Request getting is " + request.toString());
            return request;
        }
    }
}

package net.convnet.server.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.convnet.server.protocol.*;
import net.convnet.server.session.Session;
import net.convnet.server.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CVN主逻辑处理管道
 * ChannelInboundHandlerAdapter是 Netty 提供的用于处理入站数据的抽象类。
 * 它实现了 ChannelInboundHandler 接口，并提供了一组默认的方法实现。
 *
 * @author Administrator
 * @date 2024/01/02
 */
final class ConvnetHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(ConvnetHandler.class);
    private final ProtocolFactory protocolFactory;
    private final SessionManager sessionManager;
    private final FilterChain filterChain;

    ConvnetHandler(ProtocolFactory protocolFactory, SessionManager sessionManager, FilterChain filterChain) {
        this.protocolFactory = protocolFactory;
        this.sessionManager = sessionManager;
        this.filterChain = filterChain;
    }

    /**
     * 频道注册
     *
     * @param ctx CTX公司
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        this.sessionManager.createAnonymousSession(ctx.channel());
    }

    /**
     * 频道未注册
     *
     * @param ctx CTX公司
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        Session session = this.sessionManager.getSession(ctx.channel());
        if (session != null) {
            session.destory();
        }

    }

    /**
     * 通道读取
     *
     * @param ctx 处理器上下文
     * @param msg 读取到的数据对象
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //msg强转为Request对象
        Request request = (Request) msg;
        //获取request的指令
        Cmd cmd = request.getCmd();
        //若发现对应错误指令
        if (cmd == Cmd.ERROR) {
            //直接写回，代表收到
            ctx.write(request);
        } else {
            //通过ctx的频道获取session
            Session session = this.sessionManager.getSession(ctx.channel());
            //如果session获取到了，且session是登录态，或者它发送的是豁免类的指令（如登录，注册等不需要登录）
            if (session != null && (session.isLogin() || Cmd.ANONYMOUS_CMDS.contains(cmd))) {
                //获取对应协议版本（目前是1，也就是说，曾经对应协议是有多个的）的协议实现
                Protocol protocol = this.protocolFactory.getProtocol(request.getVersion());
                //根据对应实现创建回复消息
                Response response = protocol.createResponse(cmd);

                try {
                    this.filterChain.doFilter(session, request, response);
                } catch (Throwable var9) {
                    if (LOG.isInfoEnabled()) {
                        LOG.error("Process cmd [" + cmd + "] error", var9);
                    }

                    response.setSuccess(false);
                }

                if (response.needOutput()) {
                    ctx.write(response);
                }

            }
        }
    }

    /**
     * 通道读取完成
     *
     * @param ctx CTX公司
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 捕获异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        if (LOG.isInfoEnabled()) {
            LOG.error("Serve error", e);
        }

    }
}

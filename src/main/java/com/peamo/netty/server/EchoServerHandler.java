package com.peamo.netty.server;

import com.peamo.consts.HostInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author long
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 客户端连接成功会调用此方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String msg = "已连接到服务器";
        System.out.println(msg);
        ByteBuf byteBuf = Unpooled.buffer(msg.getBytes().length);
        byteBuf.writeBytes(msg.getBytes());
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        String word = byteBuf.toString(CharsetUtil.UTF_8).trim();

        if(HostInfo.EXIT.equals(word)) {

            ByteBuf buf = Unpooled.buffer(word.getBytes().length);
            buf.writeBytes(word.getBytes());
            ctx.writeAndFlush(buf);
            ctx.close();
        }else {
            String echoMsg = "[ECHO]"+word+HostInfo.SEPERATE;
            ByteBuf buf = Unpooled.buffer(echoMsg.getBytes().length);
            buf.writeBytes(echoMsg.getBytes());
            ctx.writeAndFlush(buf);
        }
        //释放缓存，写在Netty中已经有了，只需要处理读的
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

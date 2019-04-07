package com.peamo.netty.client;

import com.peamo.consts.HostInfo;
import com.peamo.uitl.InputUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * @author long
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;

        String words = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.print(words);
        if(HostInfo.EXIT.equals(words)) {
            ctx.close();
        }else {
            String input = InputUtil.getInputStr("");
            ByteBuf buf = Unpooled.buffer(input.getBytes().length);
            buf.writeBytes(input.getBytes());
            ctx.writeAndFlush(buf);
        }

        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

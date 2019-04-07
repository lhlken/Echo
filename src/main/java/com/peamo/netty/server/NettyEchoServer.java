package com.peamo.netty.server;

import com.peamo.consts.HostInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author long
 */
public class NettyEchoServer {

    public static void main(String[] args) {
        new NettyEchoServer().run();
    }

    public void run() {
        //开辟2个主线程（接收客户端连接）与4个work线程（处理客户端连接）
        EventLoopGroup boss = new NioEventLoopGroup(2);
        EventLoopGroup worker = new NioEventLoopGroup(4);
        try {
            //创建服务端类
            ServerBootstrap server = new ServerBootstrap();
            //设置线程池及channel类型
            server.group(boss,worker).channel(NioServerSocketChannel.class);
            //接收到消息后进行的处理
            server.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new EchoServerHandler());
                }
            });

            //通过常量设置TCP协议的相关配置
            server.option(ChannelOption.SO_BACKLOG,128);
            server.option(ChannelOption.SO_KEEPALIVE,true);

            ChannelFuture future = server.bind(HostInfo.PORT);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

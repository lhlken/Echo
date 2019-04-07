package com.peamo.aio.server;

import com.peamo.consts.HostInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AioMain {

    public static void main(String[] args) {
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//        boolean flag = true;
//        try {
//            AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
//
//            serverChannel.bind(new InetSocketAddress(HostInfo.PORT));
//
//            while (flag) {
//                executor.submit(new Thread(new AioServer(serverChannel)));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        executor.shutdown();

        try {
            //服务器通道
            AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
            //绑定端口
            serverChannel.bind(new InetSocketAddress(HostInfo.PORT));
            new Thread(new AioServer(serverChannel)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

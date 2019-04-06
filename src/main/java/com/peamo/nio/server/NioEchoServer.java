package com.peamo.nio.server;

import com.peamo.consts.HostInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioEchoServer {
    public static void main(String[] args) {
        //使用线程池
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            //NIO的处理基于channel，
            ServerSocketChannel server = ServerSocketChannel.open();
            //设置为非阻塞
            server.configureBlocking(false);
            //绑定端口
            server.bind(new InetSocketAddress(HostInfo.PORT));
            //设置一个Selector
            Selector selector = Selector.open();
            //将channel注册到selector
            server.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("服务器已启动");

            int selectKey = 0;
            //实现轮训处理
            while ((selectKey = selector.select()) > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for(SelectionKey key : selectionKeys) {
                    if(key.isAcceptable()) {//为连接模式
                        //等待连接
                        SocketChannel socketChannel = server.accept();
                        executor.submit(new Thread(new NioEchoHandler(socketChannel)));
                    }
                }
            }
            executor.shutdown();
            server.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class NioEchoHandler implements Runnable {

        private SocketChannel client;

        private boolean flag;

        public NioEchoHandler(SocketChannel client) {
            this.client = client;
            this.flag = true;

            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(50);
                byteBuffer.clear();
                byteBuffer.put("已连接到服务器".getBytes());
                byteBuffer.flip();
                this.client.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(50);
            try {
                while (this.flag) {
                    byteBuffer.clear();

                    int length = this.client.read(byteBuffer);
                    String inputStr = new String(byteBuffer.array(),0,length).trim();
                    if(HostInfo.EXIT.equals(inputStr)) {
                        this.flag = false;
                    }
                    String outputStr = "[echo]"+inputStr;
                    byteBuffer.clear();
                    byteBuffer.put(outputStr.getBytes());
                    byteBuffer.flip();
                    this.client.write(byteBuffer);
                }
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

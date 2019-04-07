package com.peamo.aio.server;

import com.peamo.consts.HostInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AioServer implements Runnable {

    private AsynchronousServerSocketChannel serverChannel = null;

    private CountDownLatch latch = null;

    public AioServer(AsynchronousServerSocketChannel serverChannel) {
        this.serverChannel = serverChannel;
        this.latch = new CountDownLatch(1);
        System.out.println("服务器启动成功");
    }

    public AsynchronousServerSocketChannel getServerChannel() {
        return serverChannel;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    @Override
        public void run() {
            this.serverChannel.accept(this,new AcceptHandler());
            try {
                this.latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
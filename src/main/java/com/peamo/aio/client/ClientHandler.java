package com.peamo.aio.client;

import com.peamo.consts.HostInfo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CountDownLatch;

public class ClientHandler implements Runnable {

    private AsynchronousSocketChannel socketChannel;

    private CountDownLatch latch;

    public ClientHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.latch = new CountDownLatch(1);
        this.socketChannel.connect(new InetSocketAddress(HostInfo.HOST,HostInfo.PORT));
    }

    @Override
    public void run() {
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String msg) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        byteBuffer.put(msg.getBytes());
        byteBuffer.flip();
        this.socketChannel.write(byteBuffer,byteBuffer,new ClientWriteHandler(this.socketChannel,this.latch));
        if(HostInfo.EXIT.equals(msg)) {
            this.latch.countDown();
            return false;
        }else {
            return true;
        }
    }
}

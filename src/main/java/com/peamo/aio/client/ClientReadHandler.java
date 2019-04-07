package com.peamo.aio.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class ClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;

    private CountDownLatch latch;

    public ClientReadHandler(AsynchronousSocketChannel socketChannel, CountDownLatch latch) {
        this.socketChannel = socketChannel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        String msg = new String(attachment.array(),0,attachment.remaining());
        System.out.println(msg);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.latch.countDown();
    }
}

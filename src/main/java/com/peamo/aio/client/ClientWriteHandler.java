package com.peamo.aio.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class ClientWriteHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;

    private CountDownLatch latch;

    public ClientWriteHandler(AsynchronousSocketChannel socketChannel, CountDownLatch latch) {
        this.socketChannel = socketChannel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {

        if(attachment.hasRemaining()) {
            this.socketChannel.write(attachment,attachment,new ClientWriteHandler(this.socketChannel,this.latch));
        }else {
            ByteBuffer readBuffer = ByteBuffer.allocate(100);
            this.socketChannel.read(readBuffer,readBuffer,new ClientReadHandler(this.socketChannel,this.latch));
        }


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

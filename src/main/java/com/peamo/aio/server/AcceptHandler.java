package com.peamo.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 连接接收的回调处理
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel,AioServer> {

    /**
     * Invoked when an operation has completed.
     *
     * @param result     The result of the I/O operation.
     * @param attachment
     */
    @Override
    public void completed(AsynchronousSocketChannel result, AioServer attachment) {
        System.out.println("客户端已连接");
        //接收连接
        attachment.getServerChannel().accept(attachment,this);
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        result.read(byteBuffer,byteBuffer,new EchoHandler(result));
    }

    /**
     * Invoked when an operation fails.
     *
     * @param exc        The exception to indicate why the I/O operation failed
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, AioServer attachment) {
        System.out.println("客户端连接失败");
        attachment.getLatch().countDown();
    }
}

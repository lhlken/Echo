package com.peamo.aio.server;

import com.peamo.consts.HostInfo;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class EchoHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel clientChannel;

    private boolean flag;

    public EchoHandler(AsynchronousSocketChannel clientChannel) {
        this.clientChannel = clientChannel;
        this.flag = true;
    }

    /**
     * Invoked when an operation has completed.
     *
     * @param result     The result of the I/O operation.
     * @param attachment
     */
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        String strMessage = new String(attachment.array(),0,attachment.remaining()).trim();
        String resultMessage= "[ECHO]"+strMessage+HostInfo.SEPERATE;
        System.out.println(strMessage);
        if(HostInfo.EXIT.equals(strMessage)) {
            this.flag = false;
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        byteBuffer.put(resultMessage.getBytes());
        byteBuffer.flip();
        this.clientChannel.write(byteBuffer,byteBuffer, new CompletionHandler<Integer, Buffer>() {
            /**
             * Invoked when an operation has completed.
             *
             * @param result     The result of the I/O operation.
             * @param attachment
             */
            @Override
            public void completed(Integer result, Buffer attachment) {
                if(attachment.hasRemaining()) {
                    EchoHandler.this.clientChannel.write(byteBuffer,byteBuffer,this);
                }else {
                    if(EchoHandler.this.flag == true) {
                        ByteBuffer byteBuffer1 = ByteBuffer.allocate(100);
                        EchoHandler.this.clientChannel.read(byteBuffer1,byteBuffer1,new EchoHandler(EchoHandler.this.clientChannel));
                    }
                }
            }

            /**
             * Invoked when an operation fails.
             *
             * @param exc        The exception to indicate why the I/O operation failed
             * @param attachment
             */
            @Override
            public void failed(Throwable exc, Buffer attachment) {
                try {
                    EchoHandler.this.clientChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /**
     * Invoked when an operation fails.
     *
     * @param exc        The exception to indicate why the I/O operation failed
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

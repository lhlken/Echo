package com.peamo.nio.client;

import com.peamo.consts.HostInfo;
import com.peamo.uitl.InputUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioEchoClient {

    public static void main(String[] args) {
        try {
            //NIO是基于Channel的模式
            SocketChannel client = SocketChannel.open(new InetSocketAddress(HostInfo.HOST,HostInfo.PORT));

            ByteBuffer byteBuffer = ByteBuffer.allocate(50);

            boolean flag = true;

            while (flag) {
                byteBuffer.clear();
                int length = client.read(byteBuffer);
                String outStr = new String(byteBuffer.array(),0,length);
                System.out.println(outStr);

                String msg = InputUtil.getInputStr("");
                if(HostInfo.EXIT.equals(msg)) {
                    flag = false;
                }
                byteBuffer.clear();
                byteBuffer.put(msg.getBytes());
                byteBuffer.flip();

                client.write(byteBuffer);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

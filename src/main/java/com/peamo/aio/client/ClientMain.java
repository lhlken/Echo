package com.peamo.aio.client;

import com.peamo.uitl.InputUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

public class ClientMain {
    public static void main(String[] args) {
        try {
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
            ClientHandler client = new ClientHandler(socketChannel);
            new Thread(client).start();
            while (client.sendMessage(InputUtil.getInputStr(""))) {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

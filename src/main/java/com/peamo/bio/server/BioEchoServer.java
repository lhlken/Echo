package com.peamo.bio.server;

import com.peamo.consts.HostInfo;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioEchoServer {

    public static void main(String[] args) {
        try {
            //启动服务端，并设置端口
            ServerSocket server = new ServerSocket(HostInfo.PORT);
            System.out.println("服务器已启动");
            //状态标记
            boolean flag = true;
            //使用定长线程池
            ExecutorService executor = Executors.newFixedThreadPool(2);
            while (flag) {
                executor.submit(new Thread(new BioHandler(server.accept())));
            }
            executor.shutdown();
            server.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static class BioHandler implements Runnable {

        private Socket server;
        //服务端接收流
        private Scanner scanner;
        //服务端输出流
        private PrintStream printStream;
        //状态标记
        private boolean flag;

        public BioHandler(Socket server) {
            this.server = server;
            try {
                this.scanner = new Scanner(this.server.getInputStream());
                this.scanner.useDelimiter(HostInfo.SEPERATE);
                this.printStream = new PrintStream(this.server.getOutputStream());
                this.flag = true;
                this.printStream.println("已连接到服务器");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (this.flag) {
                if(this.scanner.hasNext()) {
                    //收取client信息
                    String word = this.scanner.next().trim();
                    if(HostInfo.EXIT.equals(word)) {
                        this.flag = false;
                    }
                    //回信息给client
                    this.printStream.println("[ECHO]"+word);

                }
            }
        }
    }
}

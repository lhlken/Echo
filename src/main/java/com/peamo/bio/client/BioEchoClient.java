package com.peamo.bio.client;

import com.peamo.consts.HostInfo;
import com.peamo.uitl.InputUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class BioEchoClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HostInfo.HOST,HostInfo.PORT);
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            boolean flag = true;
            while (flag) {
                if(scanner.hasNext()) {
                    String word = scanner.next();
                    System.out.println(word);
                }
                String str = InputUtil.getInputStr("");

                if(HostInfo.EXIT.equals(str)) {
                    flag = false;
                }
                printStream.println(str);


            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

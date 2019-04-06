package com.peamo.uitl;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
public class InputUtil {

    private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));

    private InputUtil() {
    }

    /**
     * 从键盘中读取内容
     * @param prompt
     * @return
     */
    public static String getInputStr(String prompt) {
        boolean flag = true;
        String str = null;
        while (flag) {
            System.out.print(prompt);
            try {
                str = KEYBOARD_INPUT.readLine().trim();
                if(StringUtils.isEmpty(str)) {
                    System.out.println("输入错误!");
                }else {
                    flag = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static void main(String[] args) {
        getInputStr("请输入年龄:");
    }
}

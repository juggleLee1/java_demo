package com.ms.demo.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * ClassName: TCPClient1
 * Package: com.ms.demo.network
 * Description:
 *
 * @author Lee
 * @version 1.0
 * @create 2024/4/9 19:21
 */
public class TCPClient1 {
    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost", 8000);OutputStream outputStream = socket.getOutputStream();) {
            Scanner scanner = new Scanner(System.in);
            String str;
            while (true) {
                str = scanner.next();
                outputStream.write(str.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

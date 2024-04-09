package com.ms.demo.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ClassName: TCPTest
 * Package: com.ms.demo.network
 * Description:
 *
 * @author Lee
 * @version 1.0
 * @create 2024/4/9 19:14
 */
public class TCPServer1 {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8000);
            System.out.println("服务器建立连接");

            while (true) {
                Socket socket = serverSocket.accept();
                handleData(socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }

    private static void handleData(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        while (true) {
            byte[] buffer = new byte[1024];
            int len = inputStream.read(buffer);
            System.out.println("接受消息：" + new String(buffer, 0, len));
        }

    }
}

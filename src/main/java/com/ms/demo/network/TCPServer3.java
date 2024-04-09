package com.ms.demo.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * ClassName: TCPServer3
 * Package: com.ms.demo.network
 * Description:
 *
 * @author Lee
 * @version 1.0
 * @create 2024/4/9 19:53
 */
public class TCPServer3 {
    public static void main(String[] args) throws IOException {
        // 打开一个选择器
        Selector selector = Selector.open();
        // 打开服务器套接字通道,相当于 ServerSocket
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 将服务器套接字通道绑定到本地地址
        serverChannel.bind(new InetSocketAddress(8000));
        // 设置为非阻塞模式，作用是accept不阻塞
        serverChannel.configureBlocking(false);
        // 将服务器套接字通道注册到选择器，通过epoll实例监听连接事件
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务器启动，监听端口: 8000");

        while (true) {
            // 检查是否有事件准备好，没有事件发生会阻塞（节省了CPU资源）。
            selector.select();
            // 获取选择键集  获取事件集
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                // 如果是accept事件完成 否则是读事件完成
                if (key.isAcceptable()) {
                    // 接受客户端连接请求
                    SocketChannel client = serverChannel.accept();
                    // 保证读事件不阻塞
                    client.configureBlocking(false);
                    // 将新连接的通道注册到选择器
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("接受新的客户端连接: " + client);
                } else if (key.isReadable()) {
                    // 读取数据
                    SocketChannel client = (SocketChannel) key.channel();
                    // 可替换为线程池
                    handleData(client);
                }
            }
        }
    }

    private static void handleData(SocketChannel socket) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = socket.read(buffer);
        if (bytesRead > 0) {
            // 切换读写模式
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            System.out.println();
        } else if (bytesRead == -1) {
            // 客户端关闭连接
            socket.close();
        }

    }
}

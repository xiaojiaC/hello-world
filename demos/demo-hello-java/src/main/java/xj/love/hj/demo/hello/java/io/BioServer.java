package xj.love.hj.demo.hello.java.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 阻塞IO服务器
 *
 * @author xiaojia
 * @since 1.0
 */
public class BioServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(8080)); // 监听 8080 端口进来的 TCP 链接

        while (true) {
            SocketChannel sc = ssc.accept(); // 这里会阻塞，直到有一个请求的连接进来

            SocketHandler handler = new SocketHandler(sc);
            new Thread(handler).start(); // 每个请求开启一个线程处理
        }
    }

    /**
     * 业务逻辑处理器
     */
    private static class SocketHandler implements Runnable {

        private SocketChannel socketChannel;

        SocketHandler(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            try {
                // 将请求数据读入 Buffer 中
                int num;
                while ((num = socketChannel.read(buffer)) > 0) {
                    // 读取 Buffer 内容之前先 flip 一下
                    buffer.flip();

                    // 提取 Buffer 中的数据
                    byte[] bytes = new byte[num];
                    buffer.get(bytes);

                    String re = new String(bytes, "UTF-8");
                    System.out.println("接收到客户端：" + re);

                    // 回应客户端
                    ByteBuffer writeBuffer = ByteBuffer
                            .wrap(("pang " + re).getBytes());
                    socketChannel.write(writeBuffer);

                    buffer.clear();
                }
            } catch (IOException e) {
                if (socketChannel != null) {
                    try {
                        socketChannel.close();
                    } catch (final IOException ioe) {
                        // ignored
                    }
                }
            }
        }
    }
}

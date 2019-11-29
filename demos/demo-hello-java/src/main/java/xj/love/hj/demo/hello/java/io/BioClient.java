package xj.love.hj.demo.hello.java.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 阻塞IO客户端
 *
 * @author xiaojia
 * @since 1.0
 */
public class BioClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8080));

        // 发送请求
        ByteBuffer buffer = ByteBuffer.wrap((System.currentTimeMillis() + " ping").getBytes(
                StandardCharsets.UTF_8));
        socketChannel.write(buffer);

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        int num;
        while ((num = socketChannel.read(readBuffer)) > 0) {
            readBuffer.flip();

            byte[] re = new byte[num];
            readBuffer.get(re);

            String result = new String(re, "UTF-8");
            System.out.println("服务器返回: " + result);
        }
    }
}

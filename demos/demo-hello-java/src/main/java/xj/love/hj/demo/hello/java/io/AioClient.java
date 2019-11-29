package xj.love.hj.demo.hello.java.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;
import xj.love.hj.demo.hello.java.io.AioServer.Attachment;

/**
 * 异步IO客户端
 *
 * @author xiaojia
 * @since 1.0
 */
public class AioClient {

    public static void main(String[] args) throws Exception {
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        Future<?> future = client.connect(new InetSocketAddress("localhost", 8080));
        // 阻塞一下，等待连接成功
        future.get();

        Attachment att = new Attachment();
        att.setClient(client);
        att.setReadMode(false);
        att.setBuffer(ByteBuffer.allocate(2048));
        byte[] data = (System.currentTimeMillis() + " ping").getBytes(StandardCharsets.UTF_8);
        att.getBuffer().put(data);
        att.getBuffer().flip();

        // 异步发送数据到服务端
        client.write(att.getBuffer(), att, new ClientChannelHandler());

        // 这里休息一下再退出，给出足够的时间处理数据
        Thread.sleep(2000);
    }

    /**
     * 异步IO操作结果处理器
     */
    private static class ClientChannelHandler implements CompletionHandler<Integer, Attachment> {

        @Override
        public void completed(Integer result, Attachment att) {
            ByteBuffer buffer = att.getBuffer();
            if (att.isReadMode()) {
                // 读取来自服务端的数据
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                String msg = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("服务器返回: " + msg);

                // 接下来，有以下两种选择:
                // 1. 向服务端发送新的数据
//                att.setReadMode(false);
//                buffer.clear();
//                String newMsg = "new message from client";
//                byte[] data = newMsg.getBytes(Charset.forName("UTF-8"));
//                buffer.put(data);
//                buffer.flip();
//                att.getClient().write(buffer, att, this);
                // 2. 关闭连接
                try {
                    att.getClient().close();
                } catch (IOException e) {
                }
            } else {
                // 写操作完成后，会进到这里切换成读模式
                att.setReadMode(true);
                buffer.clear();
                att.getClient().read(buffer, att, this);
            }
        }

        @Override
        public void failed(Throwable t, Attachment att) {
            System.out.println("服务器无响应");
        }
    }
}

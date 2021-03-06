package xj.love.hj.demo.hello.java.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * 异步IO服务器
 *
 * @author xiaojia
 * @since 1.0
 */
public class AioServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 实例化，并监听端口
        AsynchronousServerSocketChannel server =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8080));

        // 自己定义一个 Attachment 类，用于传递一些信息
        Attachment att = new Attachment();
        att.setServer(server);

        server.accept(att, new CompletionHandler<AsynchronousSocketChannel, Attachment>() {

            @Override
            public void completed(AsynchronousSocketChannel client, Attachment att) {
                try {
                    SocketAddress clientAddr = client.getRemoteAddress();
                    System.out.println("收到新的连接：" + clientAddr);

                    // 收到新的连接后，server 应该重新调用 accept 方法等待新的连接进来
                    att.getServer().accept(att, this);

                    Attachment newAtt = new Attachment();
                    newAtt.setServer(server);
                    newAtt.setClient(client);
                    newAtt.setReadMode(true);
                    newAtt.setBuffer(ByteBuffer.allocate(2048));

                    client.read(newAtt.getBuffer(), newAtt, new ChannelHandler());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable t, Attachment att) {
                System.out.println("accept failed");
            }
        });

        Thread.currentThread().join();
    }

    /**
     * 附加到IO操作的对象
     */
    public static class Attachment {

        private AsynchronousServerSocketChannel server;
        private AsynchronousSocketChannel client;
        private boolean isReadMode;
        private ByteBuffer buffer;

        public AsynchronousServerSocketChannel getServer() {
            return server;
        }

        public void setServer(AsynchronousServerSocketChannel server) {
            this.server = server;
        }

        public AsynchronousSocketChannel getClient() {
            return client;
        }

        public void setClient(AsynchronousSocketChannel client) {
            this.client = client;
        }

        public boolean isReadMode() {
            return isReadMode;
        }

        public void setReadMode(boolean readMode) {
            isReadMode = readMode;
        }

        public ByteBuffer getBuffer() {
            return buffer;
        }

        public void setBuffer(ByteBuffer buffer) {
            this.buffer = buffer;
        }
    }

    /**
     * 异步IO操作结果处理器
     */
    private static class ChannelHandler implements CompletionHandler<Integer, Attachment> {

        @Override
        public void completed(Integer result, Attachment att) {
            if (att.isReadMode()) {
                // 读取来自客户端的数据
                ByteBuffer buffer = att.getBuffer();
                buffer.flip();

                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                String msg = new String(buffer.array(), StandardCharsets.UTF_8).trim();
                System.out.println("收到来自客户端的数据: " + msg);

                // 响应客户端请求，返回数据
                buffer.clear();
                buffer.put(("pang " + msg).getBytes(StandardCharsets.UTF_8));
                att.setReadMode(false);
                buffer.flip();

                // 写数据到客户端也是异步
                att.getClient().write(buffer, att, this);
            } else {
                // 到这里，说明往客户端写数据也结束了，有以下两种选择:
                // 1. 继续等待客户端发送新的数据过来
//                att.setReadMode(true);
//                att.getBuffer().clear();
//                att.getClient().read(att.getBuffer(), att, this);
                // 2. 既然服务端已经返回数据给客户端，断开这次的连接
                try {
                    att.getClient().close();
                } catch (IOException e) {
                }
            }
        }

        @Override
        public void failed(Throwable t, Attachment att) {
            System.out.println("连接断开");
        }
    }
}

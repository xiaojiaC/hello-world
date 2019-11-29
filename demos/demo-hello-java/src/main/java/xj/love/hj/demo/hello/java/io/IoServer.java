package xj.love.hj.demo.hello.java.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Socket通信服务器端
 *
 * @author xiaojia
 * @since 1.0
 */
public class IoServer {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);

        while (true) {
            Socket s = ss.accept(); // 这里会阻塞，直到有一个请求的连接进来
            System.out.println("客户端:" + s.getInetAddress().getLocalHost() + "已连接到服务器");

            new Thread(new SocketHandler(s)).start();
        }
    }

    /**
     * 业务逻辑处理器
     */
    private static class SocketHandler implements Runnable {

        private Socket socket;

        SocketHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 读取客户端发送来的消息
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                String message = br.readLine();
                System.out.println("接收到客户端：" + message);

                // 发送消息到客户端
                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                bw.write("pang" + message + "\n");
                bw.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

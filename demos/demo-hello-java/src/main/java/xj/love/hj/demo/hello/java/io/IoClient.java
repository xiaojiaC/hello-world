package xj.love.hj.demo.hello.java.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Socket通信客户端
 *
 * @author xiaojia
 * @since 1.0
 */
public class IoClient {

    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 8080);

            // 向服务器端发送一条消息
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));
            bw.write(System.currentTimeMillis() + " ping\n");
            bw.flush();

            // 读取服务器返回的消息
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            String mess = br.readLine();
            System.out.println("服务器返回：" + mess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

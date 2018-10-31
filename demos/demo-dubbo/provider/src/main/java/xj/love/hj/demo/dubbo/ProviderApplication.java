package xj.love.hj.demo.dubbo;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 生产者应用。
 *
 * @author xiaojia
 * @since 1.0
 */
@SpringBootApplication
public class ProviderApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ProviderApplication.class, args);
        // press any key to exit
        System.in.read();
    }

}

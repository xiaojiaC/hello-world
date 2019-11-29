package xj.love.hj.demo.websocket.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用一个简单的基于内存的消息代理，以在“/topic”端点将消息传回客户端
        config.enableSimpleBroker("/topic");
        // 定义所有消息映射端点前缀
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册“/demo-websocket”端点，启用SockJS后备选项，以便在WebSocket不可用时可以使用回退策略传输
        registry.addEndpoint("/demo-websocket").setAllowedOrigins("*").withSockJS();
    }
}

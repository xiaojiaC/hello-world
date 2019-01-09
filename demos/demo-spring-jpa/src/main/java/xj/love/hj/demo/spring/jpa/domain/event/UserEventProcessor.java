package xj.love.hj.demo.spring.jpa.domain.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 用户域事件处理器
 *
 * @author xiaojia
 * @since 1.0
 */
@Slf4j
@Component
public class UserEventProcessor {

    @Async
    @EventListener
    public void process(UserCreatedEvent event) {
        log.info("Domain event process: " + event.toString());
    }
}

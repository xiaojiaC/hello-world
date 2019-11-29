package xj.love.hj.demo.websocket.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import xj.love.hj.demo.websocket.web.form.HelloMessageForm;
import xj.love.hj.demo.websocket.web.vo.GreetingVo;

/**
 * 问候语控制器
 *
 * @author xiaojia
 * @since 1.0
 */
@Controller
public class GreetingController {

    private static final int SIMULATED_DELAY_MILLIS = 1000;

    @Value("${server.port}")
    private int port;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public GreetingVo greeting(HelloMessageForm message) throws Exception {
        // 模拟延迟
        Thread.sleep(SIMULATED_DELAY_MILLIS);
        // 返回值将广播给@SendTo注解中“/topic/greetings”目标的所有订阅者
        return new GreetingVo("您好, " + HtmlUtils.htmlEscape(message.getName()) + "! 来自: " + port);
    }
}

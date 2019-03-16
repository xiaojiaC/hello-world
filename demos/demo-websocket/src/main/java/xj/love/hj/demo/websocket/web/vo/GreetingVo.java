package xj.love.hj.demo.websocket.web.vo;

/**
 * 问候语视图对象
 *
 * @author xiaojia
 * @since 1.0
 */
public class GreetingVo {

    private String content;

    public GreetingVo() {
    }

    public GreetingVo(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

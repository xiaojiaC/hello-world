package xj.love.hj.demo.websocket.web.form;

/**
 * 欢迎消息表单
 *
 * @author xiaojia
 * @since 1.0
 */
public class HelloMessageForm {

    private String name;

    public HelloMessageForm() {
    }

    public HelloMessageForm(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

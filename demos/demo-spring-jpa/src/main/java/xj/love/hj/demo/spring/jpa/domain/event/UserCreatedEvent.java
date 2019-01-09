package xj.love.hj.demo.spring.jpa.domain.event;

import lombok.ToString;

/**
 * 用户被创建域事件
 *
 * @author xiaojia
 * @since 1.0
 */
@ToString
public class UserCreatedEvent {

    private String firstName;

    private String lastName;

    public UserCreatedEvent(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

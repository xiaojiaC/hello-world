package xj.love.hj.demo.spring.jpa.dto;

import lombok.Value;

/**
 * 用户数据传输对象
 *
 * @author xiaojia
 * @since 1.0
 */
@Value
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
}

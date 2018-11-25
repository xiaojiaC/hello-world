package xj.love.hj.demo.spring.session.common.dto;

import java.util.Date;
import lombok.Data;

/**
 * 账户数据传输对象
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
public class AccountDto {

    private Long id;

    private String username;

    private String password;

    private String salt;

    private Date lastLoggedAt;

    private Date createdAt;

    private Long createdBy;

    private Date updatedAt;

    private Long updatedBy;
}

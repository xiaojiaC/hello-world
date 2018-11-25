package xj.love.hj.demo.spring.session.web.account.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录表单
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@ApiModel(description = "登录表单")
public class LoginForm {

    @NotBlank(message = "account.username.not.blank")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @NotBlank(message = "account.password.not.blank")
    @ApiModelProperty(value = "密码", required = true)
    private String password;
}

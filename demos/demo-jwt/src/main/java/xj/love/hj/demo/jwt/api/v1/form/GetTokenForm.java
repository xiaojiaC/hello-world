package xj.love.hj.demo.jwt.api.v1.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取账户token表单
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@ApiModel
public class GetTokenForm {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;
}

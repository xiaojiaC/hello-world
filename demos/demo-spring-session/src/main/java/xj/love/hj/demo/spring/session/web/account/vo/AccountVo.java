package xj.love.hj.demo.spring.session.web.account.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 账户视图对象
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@ApiModel(description = "用户信息")
public class AccountVo {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoggedAt;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "更新人")
    private Long updatedBy;
}

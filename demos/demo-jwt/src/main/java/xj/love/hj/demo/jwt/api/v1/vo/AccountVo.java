package xj.love.hj.demo.jwt.api.v1.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 账户详情
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@ApiModel
public class AccountVo {

    @ApiModelProperty("id")
    private long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;
}

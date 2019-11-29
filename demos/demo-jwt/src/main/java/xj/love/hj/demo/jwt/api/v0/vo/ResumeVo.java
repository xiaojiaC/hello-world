package xj.love.hj.demo.jwt.api.v0.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 账户详情值对象
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@ApiModel
public class ResumeVo {

    @ApiModelProperty("id")
    private long id;

    @ApiModelProperty("账户id")
    private long accountId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("性别")
    private int gender;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("电子邮件")
    private String email;

    @ApiModelProperty("家庭住址")
    private String homeAddress;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;
}

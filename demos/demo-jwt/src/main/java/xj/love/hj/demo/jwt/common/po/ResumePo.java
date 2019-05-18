package xj.love.hj.demo.jwt.common.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * 个人简历
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@Entity
@Table(name = "dj_resume")
public class ResumePo extends BasePo {

    @Column(name = "account_id")
    private long accountId;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private int gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "home_address")
    private String homeAddress;

}

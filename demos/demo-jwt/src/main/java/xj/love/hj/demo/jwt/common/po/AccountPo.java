package xj.love.hj.demo.jwt.common.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 * 账户
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@Entity
@Table(name = "dj_account")
public class AccountPo extends BasePo {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

}

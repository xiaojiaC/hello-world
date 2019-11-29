package xj.love.hj.demo.spring.session.common.po;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xj.love.hj.demo.spring.session.common.po.base.LongTypeIdPo;

/**
 * 账户
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dss_account")
public class AccountPo extends LongTypeIdPo {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "last_logged_at")
    private Date lastLoggedAt;
}

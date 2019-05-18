package xj.love.hj.demo.jwt.common.context;

import lombok.Data;
import xj.love.hj.demo.jwt.common.po.AccountPo;

/**
 * API访问者
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
public class ApiVisitor {

    private Long accountId;
    private String username;
    private AccountPo accountPo;

    public static ApiVisitor of(AccountPo accountPo) {
        ApiVisitor visitor = new ApiVisitor();
        visitor.setAccountId(accountPo.getId());
        visitor.setUsername(accountPo.getUsername());
        visitor.setAccountPo(accountPo);
        return visitor;
    }
}

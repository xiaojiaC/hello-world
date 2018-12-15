package xj.love.hj.demo.spring.session.common.context;

import java.util.Optional;
import xj.love.hj.demo.spring.session.common.dto.AccountDto;

/**
 * Web访问者
 *
 * @author xiaojia
 * @since 1.0
 */
public class WebVisitor implements Visitor {

    private AccountDto accountDto;

    public AccountDto getAccountDto() {
        return accountDto;
    }

    public void setAccountDto(AccountDto accountDto) {
        this.accountDto = accountDto;
    }

    @Override
    public Long getId() {
        return Optional.ofNullable(getAccountDto())
                .map(AccountDto::getId)
                .orElse(null);
    }
}

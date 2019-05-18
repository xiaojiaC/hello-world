package xj.love.hj.demo.jwt.api.v1.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账户token值对象
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVo {

    private String token;
}

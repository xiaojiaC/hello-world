package xj.love.hj.demo.jwt.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xj.love.hj.demo.jwt.common.configuration.properties.JwtProperties;
import xj.love.hj.demo.jwt.common.po.AccountPo;

/**
 * JWT Token工具类
 *
 * <pre>
 * JWT:
 *      Header（头部）.Payload（负载）.Signature（签名）
 *  Header:
 *      alg (algorithm)：签名算法
 *      typ (type)：令牌类型
 *  Payload:
 *      iss (issuer)：签发人
 *      exp (expiration time)：过期时间
 *      sub (subject)：主题
 *      aud (audience)：受众
 *      nbf (Not Before)：生效时间
 *      iat (Issued At)：签发时间
 *      jti (JWT ID)：编号
 *  Signature:
 *      对前两部分的签名，防止数据篡改
 *
 *  加密示例: token = HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
 * </pre>
 *
 * @author xiaojia
 * @see <a href="https://jwt.io/">jwt</a>
 * @see <a href="https://github.com/jwtk/jjwt">jjwt reference</a>
 * @since 1.0
 */
@Component
public class JwtTokenUtil {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 根据账户信息生成token
     */
    public String generateToken(AccountPo accountPo) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, accountPo.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        Date expirationDate = calculateExpirationDate(createdDate);

        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + jwtProperties.getExpiration());
    }

    /**
     * 校验token有效性
     */
    public boolean validateToken(String token) {
        String username = getUsernameFromToken(token);
        Date expirationDate = getExpirationDateFromToken(token);
        return !StringUtils.isEmpty(username)
                && expirationDate != null
                && expirationDate.after(new Date());
    }

    private Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从token中获取账户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从token中获取签发日期
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    /**
     * 从token中获取过期日期
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从token中获取自定义载荷对象
     *
     * @param token jwt token字符串
     * @param claimsResolver Claims->T转换函数
     * @param <T> 载荷对象类型
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        Date createdDate = new Date();
        Date expirationDate = calculateExpirationDate(createdDate);

        Claims claims = getClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }
}

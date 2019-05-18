# 如何运行

- 使用`db/schema.sql`和`db/data.sql`创建库并导入数据；
- 选择`Application.java`启动应用；
- 访问 http://localhost:8080/swagger-ui.html ；
- 访问 `/api` 空间下的api体验v0版本api，然后切换至 `/api/v1` 空间;
- 访问 `/api/v1/account/token` 填写用户名`bob`和密码`abc123`获取api token；
- 在`Authorization`授权头填写上一步获取的api token（追加前缀`Bearer `），访问该空间下的其他api。

# JWT

```
  JWT:
       Header（头部）.Payload（负载）.Signature（签名）
    Header:
       alg (algorithm)：签名算法
       typ (type)：令牌类型
    Payload:
       iss (issuer)：签发人
       exp (expiration time)：过期时间
       sub (subject)：主题
       aud (audience)：受众
       nbf (Not Before)：生效时间
       iat (Issued At)：签发时间
       jti (JWT ID)：编号
    Signature:
       对前两部分的签名，防止数据篡改
 
  JWS(use HS256):
    HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
```
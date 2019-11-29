# 如何运行

- 使用`db/schema.sql`和`db/data.sql`创建库并导入数据；
- 选择`Application.java`启动应用；
- 访问 http://localhost:8080/swagger-ui.html ；
- 填写用户名`bob`和密码`abc123_`测试登录；
- 测试登出；一次登出伴随着一次登录，再次测试登出，你将会看到未授权错误提示。

> 提示: 访问redis查看数据已了解spring session。
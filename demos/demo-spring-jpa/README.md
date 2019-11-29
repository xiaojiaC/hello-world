# 如何运行

- 修改`spring.jpa.hibernate.ddl-auto=create`（配置自动创建表）；
- 选择`Application.java`启动应用（会使用`data.json`填充数据）；
- 访问 http://localhost:8080/users 体验hateoas ；
- 访问 http://localhost:8080/users/querydsl?firstName=Bob 体验querydsl ；
- 查看并运行`UserRepositoryTests`和`UserControllerTests`以了解spring jpa特性。

> 提示: 访问[这里](https://xiaojiac.github.io/hello-world/cookbook/spring-data-jpa.html)查看Spring Data
JPA指南。
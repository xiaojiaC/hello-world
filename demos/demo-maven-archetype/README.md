# Maven Archetype

1. 安装到本地仓库: `mvn clean install archetype:update-local-catalog -P'!checkstyle'`;
2. 使用骨架快速创建应用:
    ```shell
    mvn archetype:generate \
        -DarchetypeGroupId=xj.love.hj \
        -DarchetypeArtifactId=demo-maven-archetype \
        -DarchetypeVersion=1.0.0-SNAPSHOT
    ```
3. 编译运行生成的项目，访问[demos/welcome端点](http://127.0.0.1:8080/demos/welcome)查看示例。

## 参考链接

- [Creating Archetypes](https://maven.apache.org/guides/mini/guide-creating-archetypes.html)

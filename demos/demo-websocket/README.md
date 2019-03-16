# 如何运行

- 复制`conf/nginx.conf`到本机nginx安装目录`conf`文件夹下；
- 执行`mvn clean package`构建应用jar包；
- 使用`java -jar target/demo-websocket-1.0.0-SNAPSHOT.jar`启动应用在8080端口；
- 使用`java -jar -Dserver.port=8081 target/demo-websocket-1.0.0-SNAPSHOT.jar`启动应用在8081端口；
- 在至少两个浏览器中访问 http://localhost/index.html 体验功能。

# WebSocket代理


[proxy_read_timeout]:http://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_read_timeout


要将客户端和服务器之间的连接从HTTP 1.1转换为WebSocket，请使用HTTP/1.1中提供的[协议交换](https://tools.ietf.org/html/rfc2616#section-14.42)机制。

然而，有一个微妙之处：由于“Upgrade”是逐跳([hop-by-hop](https://tools.ietf.org/html/rfc2616#section-13.5.1))标头，因此它不会从客户端传递到代理服务器。使用正向代理，客户端可以使用`CONNECT`方法来规避此问题。但是，这不适用于反向代理，因为客户端不知道任何代理服务器，并且需要在代理服务器上进行特殊处理。

从版本1.3.13开始，nginx实现了特殊的操作模式，如果代理服务器返回带有101（交换协议）代码的响应，并且客户端通过请求中的“Upgrade”报头请求协议交换，则nginx允许在客户端和代理服务器之间建立通道。

如上所述，包括“Upgrade”和“Connection”的逐跳报头不会从客户端传递到代理服务器，因此为了让代理服务器知道客户端需要将协议切换到WebSocket，这些报头必须明确传递：

```
location /chat/ {
    proxy_pass http://backend;
    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection "upgrade";
}
```

一个更复杂的示例，其中对代理服务器的请求中的“Connection”标头字段的值取决于客户端请求标头中是否存在“Upgrade”字段：

```
http {
    map $http_upgrade $connection_upgrade {
        default upgrade;
        ''      close;
    }

    server {
        ...

        location /chat/ {
            proxy_pass http://backend;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection $connection_upgrade;
        }
    }
```

默认情况下，如果代理服务器在60秒内未传输任何数据，连接将被关闭。使用[proxy_read_timeout]指令可以增加此超时时间。或者，代理服务器可以配置为周期性地发送WebSocket ping帧来重置超时并检查连接是否仍然存在。


TIP: [原文链接](http://nginx.org/en/docs/http/websocket.html)

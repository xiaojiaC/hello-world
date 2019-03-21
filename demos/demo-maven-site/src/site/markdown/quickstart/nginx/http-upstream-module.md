<!-- TOC -->
- [`upstream`](#directives-upstream)
- [`server`](#directives-server)
- [`zone`](#directives-zone)
- [`state`](#directives-state)
- [`hash`](#directives-hash)
- [`ip_hash`](#directives-ip-hash)
- [`keepalive`](#directives-keepalive)
- [`keepalive_requests`](#directives-keepalive-requests)
- [`keepalive_timeout`](#directives-keepalive-timeout)
- [`least_conn`](#directives-least-conn)
- [`least_time`](#directives-least-time)
- [`queue`](#directives-queue)
- [`random`](#directives-random)
<!-- /TOC -->

# `ngx_http_upstream_module`

`ngx_http_upstream_module` 模块用于定义可由[proxy_pass](http://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_pass)，[fastcgi_pass](http://nginx.org/en/docs/http/ngx_http_fastcgi_module.html#fastcgi_pass)，[uwsgi_pass](http://nginx.org/en/docs/http/ngx_http_uwsgi_module.html#uwsgi_pass)，[scgi_pass](http://nginx.org/en/docs/http/ngx_http_scgi_module.html#scgi_pass)，[memcached_pa​​ss](http://nginx.org/en/docs/http/ngx_http_memcached_module.html#memcached_pass)和[grpc_pass](http://nginx.org/en/docs/http/ngx_http_grpc_module.html#grpc_pass)指令引用的服务器组。

## 示例配置

```
upstream backend {
    server backend1.example.com       weight=5;
    server backend2.example.com:8080;
    server unix:/tmp/backend3;

    server backup1.example.com:8080   backup;
    server backup2.example.com:8080   backup;
}

server {
    location / {
        proxy_pass http://backend;
    }
}
```

具有定期运行健康状况检查的动态可配置组可作为我们[商业订阅](https://www.nginx.com/products/)的一部分：

```
resolver 10.0.0.1;

upstream dynamic {
    zone upstream_dynamic 64k;

    server backend1.example.com      weight=5;
    server backend2.example.com:8080 fail_timeout=5s slow_start=30s;
    server 192.0.2.1                 max_fails=3;
    server backend3.example.com      resolve;
    server backend4.example.com      service=http resolve;

    server backup1.example.com:8080  backup;
    server backup2.example.com:8080  backup;
}

server {
    location / {
        proxy_pass http://dynamic;
        health_check;
    }
}
```

## 指令

### <span id="directives-upstream">`upstream`</span>

```
Syntax:	upstream name { ... }
Default:	—
Context:	http
```

定义一组服务器。服务器可以监听不同的端口。此外，可以混合监听TCP和UNIX域套接字的服务器。

样例：

```
upstream backend {
    server backend1.example.com weight=5;
    server 127.0.0.1:8080       max_fails=3 fail_timeout=30s;
    server unix:/tmp/backend3;

    server backup1.example.com  backup;
}
```

默认情况下，使用加权轮询负载均衡策略在服务器之间分配请求。在上面的示例中，每7个请求将按如下方式分发：5个请求转发到`backend1.example.com`，另2个请求分别转发给第二个和第三个服务器。如果在与服务器通信期间发生错误，请求将被传递到下一个服务器，依此类推，直到尝试完所有正常运行的服务器。如果无法从任何服务器获得成功的响应，则客户端将接收到与最后一个服务器的通信结果。

### <span id="directives-server">`server`</span>

```
Syntax:	server address [parameters];
Default:	—
Context:	upstream
```

定义服务器的*address*和其他*parameters*。可以将地址指定为域名或IP地址，使用可选端口，或者指定为 "`unix:`" 前缀后跟UNIX域套接字路径。如果未指定端口，则使用端口80。解析为多个IP地址的域名一次定义多个服务器。

可以定义以下参数：

- `weight=number`: 设置服务器的权重，默认为1。

- `max_conns=number`: 限制与代理服务器的最大并发活动连接数（1.11.5）。默认值为零，表示没有限制。如果服务器组不驻留在[共享内存]()中，则每个工作进程的限制都有效。

    如果启用了空闲keepalive连接，多个工作进程和共享内存，则代理服务器的活动和空闲连接总数可能会超过`max_conns`值。

    自版本1.5.9和版本1.11.5之前，此参数作为我们的[商业订阅](https://www.nginx.com/products/)的一部分提供。

- `max_fails=number`: 设置应在`fail_timeout`参数设置的持续时间内发生与服务器通信失败尝试的次数，以考虑服务器在`fail_timeout`参数设置的持续时间内不可用。即在`fail_timeout`参数定义的时间段内，如果失败的次数达到此值，nginx就认为服务器不可用。在下一个`fail_timeout`时间段，服务器不会再被尝试。失败的尝试次数默认为1。设为0就会停止统计尝试次数，并认为服务器是一直可用的。你可以通过[proxy_next_upstream](http://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_next_upstream)、[fastcgi_next_upstream](http://nginx.org/en/docs/http/ngx_http_fastcgi_module.html#fastcgi_next_upstream)、[uwsgi_next_upstream](http://nginx.org/en/docs/http/ngx_http_uwsgi_module.html#uwsgi_next_upstream)、[scgi_next_upstream](http://nginx.org/en/docs/http/ngx_http_scgi_module.html#scgi_next_upstream)、[memcached_next_upstream](http://nginx.org/en/docs/http/ngx_http_memcached_module.html#memcached_next_upstream)和[grpc_next_upstream](http://nginx.org/en/docs/http/ngx_http_grpc_module.html#grpc_next_upstream)指令定义什么是失败的尝试。

- `fail_timeout=time`: 
    - 统计失败尝试次数的时间段。在这段时间中，服务器失败次数达到指定的尝试次数，服务器就被认为不可用。
    - 服务器被认为不可用的时间段。

    默认情况下，参数设置为10秒。

- `backup`: 将服务器标记为备份服务器。当主服务器不可用时，请求会被传递给这些服务器。

- `down`: 将服务器标记为永久不可用。

### <span id="directives-zone">`zone`</span>
```
Syntax:	zone name [size];
Default:	—
Context:	upstream
该指令出现在1.9.0版本中。
```

定义共享内存区域的*name*和*size*，该区域保持组工作进程之间共享的组配置和运行时状态。几个组可能共享同一个区域。在这种情况下，仅指定一次*size*就足够了。

### <span id="directives-state">`state`</span>
```
Syntax:	state file;
Default:	—
Context:	upstream
该指令出现在1.9.7版本中。
```

指定保持动态可配置组状态的*file*。

样例：

```
state /var/lib/nginx/state/servers.conf; # path for Linux
state /var/db/nginx/state/servers.conf;  # path for FreeBSD
```

状态目前仅限于带有参数的服务器列表。解析配置时将读取该文件，并在每次[更改](http://nginx.org/en/docs/http/ngx_http_api_module.html#http_upstreams_http_upstream_name_servers_)upstream配置时更新该文件。应避免直接更改文件内容。该指令不能与[server指令](#directives-server)一起使用。

[配置重新加载](http://nginx.org/en/docs/control.html#reconfiguration)或[二进制升级](http://nginx.org/en/docs/control.html#upgrade)期间所做的更改可能会丢失。

该指令作为我们[商业订阅](https://www.nginx.com/products/)的一部分提供。

### <span id="directives-hash">`hash`</span>

```
Syntax:	hash key [consistent];
Default:	—
Context:	upstream
该指令出现在1.7.2版本中。
```

指定服务器组的负载均衡方法，其中客户端-服务器映射基于`key`哈希值。`key`可以包含文本，变量及其组合。请注意，从组中添加或删除服务器可能会导致将大多数`key`重新映射到不同的服务器。该方法与[Cache::Memcached](https://metacpan.org/pod/Cache::Memcached)Perl库兼容。

如果指定了`consistent`参数，则将使用[ketama](https://www.metabrew.com/article/libketama-consistent-hashing-algo-memcached-clients)一致性哈希方法。该方法确保在向组添加服务器或从组中删除服务器时，只有少数keys将重新映射到不同的服务器。这有助于为缓存服务器实现更高的缓存命中率。该方法与[Cache::Memcached::Fast](https://metacpan.org/pod/Cache::Memcached::Fast)Perl库兼容，并将*ketama_points*参数设置为160。

### <span id="directives-ip-hash">`ip_hash`</span>

```
Syntax:	ip_hash;
Default:	—
Context:	upstream
```

指定服务器组的负载均衡方法，其中请求基于客户端IP地址在服务器之间分发。客户端IPv4地址的前三个八位字节或整个IPv6地址用作散列键。该方法确保来自同一客户端的请求将始终传递到同一服务器，除非此服务器不可用。在后一种情况下，客户端请求将被传递到另一个服务器，而且很有可能的是，它也将始终是同一台服务器。可以解决session和cookie的问题，但会导致负载不均衡。

从版本1.3.2和1.2.2开始支持IPv6地址。

如果需要临时删除其中一个服务器，则应使用`down`参数对其进行标记，以保留客户端IP地址的当前哈希值。

样例：

```
upstream backend {
    ip_hash;

    server backend1.example.com;
    server backend2.example.com;
    server backend3.example.com down;
    server backend4.example.com;
}
```

在版本1.3.1和1.2.2之前，无法使用`ip_hash`负载均衡方法为服务器指定权重。

`nginx-sticky-module`: nginx的一个扩展模块，实现了通过Cookie的会话粘贴效果。

基本原理：客户端首次发起访问请求时，首先根据round-robin随机轮询到某台后端服务器，然后在响应的Set-Cookie上加上“route=md5(upstream)”字段（可能是明文，也可能是md5、sha1等Hash值），第二次请求再处理的时候，发现有*route*字段，会直接导向原来的那个节点。

### <span id="directives-keepalive">`keepalive`</span>

```
Syntax:	keepalive connections;
Default:	—
Context:	upstream
该指令出现在1.1.4版本中。
```

激活对上游服务器的连接进行缓存。

*connections*参数设置在每个工作进程的缓存中保留的上游服务器的最大空闲keepalive连接数。如果连接数大于这个值时，将关闭最近最少使用的连接。

需要特别注意的是，`keepalive`指令不限制nginx工作进程可以打开的上游服务器的连接总数。*connections*参数应该设置的足够小，以便上游服务器也能处理新进来的连接。

使用keepalive连接的memcached上游样例配置：

```
upstream memcached_backend {
    server 127.0.0.1:11211;
    server 10.0.0.2:11211;

    keepalive 32;
}

server {
    ...

    location /memcached/ {
        set $memcached_key $uri;
        memcached_pass memcached_backend;
    }

}
```

对于HTTP，[proxy_http_version](http://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_http_version)指令应设置为“1.1”，并且应清除“Connection”头字段：

```
upstream http_backend {
    server 127.0.0.1:8080;

    keepalive 16;
}

server {
    ...

    location /http/ {
        proxy_pass http://http_backend;
        proxy_http_version 1.1;
        proxy_set_header Connection "";
        ...
    }
}
```

或者，可以通过将“Connection：Keep-Alive”头字段传递给上游服务器来使用HTTP/1.0持久连接，但不建议使用此方法。

对于FastCGI服务器，需要设置[fastcgi_keep_conn](http://nginx.org/en/docs/http/ngx_http_fastcgi_module.html#fastcgi_keep_conn)才能使keepalive连接正常工作：

```
upstream fastcgi_backend {
    server 127.0.0.1:9000;

    keepalive 8;
}

server {
    ...

    location /fastcgi/ {
        fastcgi_pass fastcgi_backend;
        fastcgi_keep_conn on;
        ...
    }
}
```

使用除默认round-robin方法之外的负载平衡器方法时，必须在`keepalive`指令之前激活它们。

SCGI和uwsgi协议没有keepalive连接的概念。

### <span id="directives-keepalive-requests">`keepalive_requests`</span>

```
Syntax:	keepalive_requests number;
Default:	
keepalive_requests 100;
Context:	upstream
该指令出现在1.15.3版本中。
```

设置可通过一个keepalive连接提供的最大请求数。在发出最大请求数后，将关闭连接。

### <span id="directives-keepalive-timeout">`keepalive_timeout`</span>

```
Syntax:	keepalive_timeout timeout;
Default:	
keepalive_timeout 60s;
Context:	upstream
该指令出现在1.15.3版本中。
```

设置超时，在此期间与上游服务器的空闲keepalive连接将保持打开状态。

### <span id="directives-least-conn">`least_conn`</span>

```
Syntax:	least_conn;
Default:	—
Context:	upstream
该指令出现在1.3.1和1.2.2版本中。
```

指定服务器组的负载均衡方法，其中请求会被转发到活动连接数最少的服务器上，同时考虑服务器的权重。如果有多个这样的服务器（活动连接数同为最小），则使用加权轮询方法依次尝试它们。

### <span id="directives-least-time">`least_time`</span>

```
Syntax:	least_time header | last_byte [inflight];
Default:	—
Context:	upstream
该指令出现在1.7.10版本中。
```

指定服务器组的负载均衡方法，其中请求会被转发到平均响应时间最小和活动连接数最少的服务器上，同时考虑服务器的权重。如果有多个这样的服务器，则使用加权轮询方法依次尝试它们。

如果指定了*header*参数，则使用接收[响应头](#upstream-header-time)的时间。如果指定了*last_byte*参数，则使用接收[完整响应](#upstream-response-time)的时间。如果指定了*inflight*参数（1.11.6），则还会考虑不完整的请求。

在1.11.6版之前，默认情况下会考虑未完成的请求。

该指令作为我们[商业订阅](https://www.nginx.com/products/)的一部分提供。

### <span id="directives-queue">`queue`</span>

```
Syntax:	queue number [timeout=time];
Default:	—
Context:	upstream
该指令出现在1.5.12版本中。
```

如果在处理请求时无法立即选择上游服务器，则请求将被置于队列中。该指令指定了可以同时在队列中的最大请求数*number*。如果队列已满，或者在*timeout*参数中指定的时间段内无法选择要传递请求的服务器，则会返回502 (Bad Gateway)错误给客户端。

*timeout*参数的默认值为60秒。

使用除默认round-robin方法之外的负载平衡器方法时，必须在`queue`指令之前激活它们。

该指令作为我们[商业订阅](https://www.nginx.com/products/)的一部分提供。

### <span id="directives-random">`random`</span>

```
Syntax:	random [two [method]];
Default:	—
Context:	upstream
该指令出现在1.15.1版本中。
```

指定服务器组的负载均衡方法，其中请求将传递给随机选择的服务器，同时考虑服务器的权重。

可选的*two*参数指示nginx随机选择[两个](https://homes.cs.washington.edu/~karlin/papers/balls.pdf)服务器，然后使用指定的*method*选择一个服务器。默认方法是`least_conn`，它将请求传递给活动连接数最少的服务器。

`least_time`方法将请求传递给平均响应时间最短且活动连接数最少的服务器。如果指定了`least_time=header`，则使用接收[响应头](#upstream-header-time)的时间。如果指定了`least_time=last_byte`，则使用接收[完整响应](#upstream-response-time)的时间。


## 内嵌变量

`ngx_http_upstream_module`模块支持以下内嵌变量：

- <span id="upstream-addr">`$upstream_addr`</span>: 保留IP地址和端口，或上游服务器的UNIX域套接字的路径。如果在请求处理期间连接了多个服务器，则它们的地址用逗号分隔，例如“`192.168.1.1:80, 192.168.1.2:80, unix:/tmp/sock`”。如果发生从一个服务器组到另一个服务器组的内部重定向，由“X-Accel-Redirect”或error_page发起，那么来自不同组的服务器地址用冒号分隔，例如，“`192.168.1.1:80, 192.168.1.2:80, unix:/tmp/sock : 192.168.10.1:80, 192.168.10.2:80`”。如果无法选择服务器，则变量将保留服务器组的名称。

- <span id="upstream-bytes-received">`$upstream_bytes_received`</span>: 从上游服务器接收的字节数（1.11.4）。来自多个连接的值由逗号和冒号分隔，如[`$upstream_addr`](#upstream-addr)变量中的地址。

- <span id="upstream-bytes-sent">`$upstream_bytes_sent`</span>: 发送到上游服务器的字节数（1.15.8）。来自多个连接的值由逗号和冒号分隔，如[`$upstream_addr`](#upstream-addr)变量中的地址。

- <span id="upstream-cache-status">`$upstream_cache_status`</span>: 保留访问响应缓存的状态（0.8.3）。状态可以是“`MISS`”，“`BYPASS`”，“`EXPIRED`”，“`STALE`”，“`UPDATING`”，“`REVALIDATED`”或“`HIT`”。

- <span id="upstream-connect-time">`$upstream_connect_time`</span>: 保留与上游服务器建立连接所花费的时间（1.9.1）；时间以秒为单位，分辨率为毫秒。在SSL的情况下，包括在握手上花费的时间。多个连接的时间用逗号和冒号分隔，如[`$upstream_addr`](#upstream-addr)变量中的地址。

- <span id="upstream-cookie-name">`$upstream_cookie_name`</span>: 上游服务器在“Set-Cookie”响应头字段（1.7.1）中发送的具有指定*name*的cookie。只保存来自最后一个服务器响应的cookie。

- <span id="upstream-header-time">`$upstream_header_time`</span>: 保留从上游服务器接收响应头所花费的时间（1.7.10）；时间以秒为单位，分辨率为毫秒。多个响应的时间用逗号和冒号分隔，如[`$upstream_addr`](#upstream-addr)变量中的地址。

- <span id="upstream-http-name">`$upstream_http_name`</span>: 保留服务器响应头字段。例如，“Server”响应头字段可通过`$upstream_http_server`变量获得。将头字段名称转换为变量名的规则与以“[$http_](http://nginx.org/en/docs/http/ngx_http_core_module.html#var_http_)”前缀开头的变量相同。只保存来自最后一个服务器响应的头字段。

- <span id="upstream-queue-time">`$upstream_queue_time`</span>: 保留请求在上游队列中所花费的时间（1.13.9）；时间以秒为单位，分辨率为毫秒。多个响应的时间用逗号和冒号分隔，如[`$upstream_addr`](#upstream-addr)变量中的地址。

- <span id="upstream-response-length">`$upstream_response_length`</span>: 保留从上游服务器获得的响应长度（0.7.27）；长度以字节为单位。多个响应的长度用逗号和冒号分隔，如[`$upstream_addr`](#upstream-addr)变量中的地址。

- <span id="upstream-response-time">`$upstream_response_time`</span>: 保留从上游服务器接收响应所花费的时间；时间以秒为单位，分辨率为毫秒。多个响应的时间用逗号和冒号分隔，如[`$upstream_addr`](#upstream-addr)变量中的地址。


- <span id="upstream-status">`$upstream_status`</span>: 保留从上游服务器获得的响应的状态码。多个响应的状态码用逗号和冒号分隔，如[`$upstream_addr`](#upstream-addr)变量中的地址。如果未选中服务器，则变量将保留502 (Bad Gateway)状态码。

- <span id="upstream-trailer-name">`$upstream_trailer_name`</span>: 保留从上游服务器获得的响应结束的字段（1.13.10）。


TIP: [原文链接](http://nginx.org/en/docs/http/ngx_http_upstream_module.html)

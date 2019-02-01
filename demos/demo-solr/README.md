# 如何运行

- 使用`db/schema.sql`和`db/data.sql`创建库并导入数据；
- 访问[这里](https://lucene.apache.org/solr/)下载并安装solr；
- 执行`bin/solr create -c product`创建核心；
- 访问[这里](https://dev.mysql.com/downloads/connector/j/)下载mysql驱动并放置在`contrib/dataimporthandler/lib`目录；
- 编辑`server/solr/product/solrconfig.xml`，配置DIH及中文分词器类库；

```
<!-- 配置数据导入库 -->
<lib dir="${solr.install.dir:../../../..}/contrib/dataimporthandler/lib" regex=".*\.jar" />
<lib dir="${solr.install.dir:../../../..}/dist/" regex="solr-dataimporthandler-\d.*\.jar" />

<!-- 配置中文分词库 -->
<lib dir="${solr.install.dir:../../../..}/contrib/analysis-extras/lucene-libs" regex="lucene-analyzers-smartcn-\d.*\.jar" />

<!-- 配置请求处理器 -->
<requestHandler name="/dataimport" class="solr.DataImportHandler">
  <lst name="defaults">
    <str name="config">data-config.xml</str>
  </lst>
</requestHandler>
```

- 创建`server/solr/product/data-config.xml`，设置数据导入配置；

```
<dataConfig>
   <dataSource type="JdbcDataSource"
               driver="com.mysql.jdbc.Driver"
               url="jdbc:mysql://localhost:3306/demo?useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull"
               readOnly="true"
               user="demo"
               password="admin" />
   <document>
       <entity name="product" pk="id"
               query="select *, convert(status,UNSIGNED) as status_i from ds_product"
               deltaQuery="select id from ds_product where update_time > '${dataimporter.last_index_time}'">
            <field column="id" name="id" />
            <field column="title" name="title" />
            <field column="description" name="description" />
            <field column="color" name="color" />
            <field column="size" name="size" />
            <field column="price" name="price" />
            <field column="inventory" name="inventory" />
            <field column="brand" name="brand" />
            <field column="to_market_time" name="to_market_at" />
            <field column="status_i" name="status" /><!-- tinyint默认会转成boolean,需要使用mysql函数手动转成int -->
            <field column="create_time" name="created_at" />
            <field column="update_time" name="updated_at" />

            <entity name="product_image" pk="id"
                    query="select image from ds_product_image where product_id = '${product.id}'"
                    deltaQuery="select id, product_id from ds_product_image where update_time > '${dataimporter.last_index_time}'"
                    parentDeltaQuery="select id from ds_product where id = '${product_image.product_id}'">
                <field column="image" name="images" />
            </entity>
       </entity>
   </document>
</dataConfig>
```

- 编辑`server/solr/product/managed-schema.xml`，配置索引schema定义；

```
<!-- 配置索引字段定义 -->
<field name="id" type="string" indexed="true" stored="true" required="true" multiValued="false" />
<field name="title" type="text_cn" indexed="true" stored="true" />
<field name="description" type="text_cn" indexed="true" stored="true" />
<field name="color" type="string" indexed="true" stored="true" />
<field name="size" type="string" indexed="true" stored="true" />
<field name="price" type="pfloat" indexed="true" stored="true" />
<field name="inventory" type="pint" indexed="true" stored="true" />
<field name="brand" type="string" indexed="true" stored="true" />
<field name="to_market_at" type="pdate" indexed="true" stored="true" />
<field name="status" type="pint" indexed="true" stored="true" />
<field name="created_at" type="pdate" indexed="true" stored="true" />
<field name="updated_at" type="pdate" indexed="true" stored="true" />
<field name="images" type="string" indexed="false" stored="true" multiValued="true" />

<field name="content" type="text_cn" indexed="true" stored="false" multiValued="true" />

<copyField source="title" dest="content" />
<copyField source="description" dest="content" />
<copyField source="color" dest="content" />
<copyField source="size" dest="content" />
<copyField source="brand" dest="content" />

<!-- 配置中文分词 -->
<fieldType name="text_cn" class="solr.TextField" positionIncrementGap="100">
  <analyzer type="index">
      <tokenizer class="org.apache.lucene.analysis.cn.smart.HMMChineseTokenizerFactory"/>
  </analyzer>
  <analyzer type="query">
      <tokenizer class="org.apache.lucene.analysis.cn.smart.HMMChineseTokenizerFactory"/>
  </analyzer>
</fieldType>

<uniqueKey>id</uniqueKey>
```

- 编辑`bin/solr.in`，修改默认时区为CCT（北京时间）

```
SOLR_TIMEZONE=CCT
```

- 执行`solr start`启动solr；
- 访问 http://localhost:8983/solr 选择`dataimport`点击`Execute`全量导入数据；若修改数据可选择`delta-import`增量式导入；
- 选择`Query`点击`Execute Query`查看已导入数据、体验solr搜索；
- 选择`Application.java`启动应用；
- 访问 http://localhost:8080/products?keyword=透气 体验solr复制字段特性；
- 访问 http://localhost:8080/products 体验solr分面特性（更多特性请自行码代码体验）；
- 访问 http://localhost:8080/products?title=裤子&page=0&size=3 体验spring存储库片段自定义特性。

> 提示: 访问[这里](https://xiaojiac.github.io/hello-world/cookbook/spring-data-solr.html)查看Spring Data
Solr指南。

## 安装目录简介

- `bin`：包括一些使用Solr的重要脚本。
    - `solr`/`solr.cmd`：分别用于Linux和Windows系统，根据所选参数不同而控制Solr的启动和停止。
    - `solr.in.sh`/`solr.in.cmd`：分别用于Linux和Windows系统的属性文件。
    - `install_solr_services.sh`：用于Linux系统将Solr作为服务安装。
    - `post`：提供了一个用于发布内容的命令行接口工具。支持导入JSON，XML和CSV，也可以导入HTML，PDF，Microsoft Office格式（如MS Word），纯文本等等。
- `contrib`：包含一些solr的插件或扩展。
    - `analysis-extras`： 包含一些文本分析组件及其依赖。
    - `clustering`：包含一个用于集群搜索结果的引擎。
    - `dataimporthandler`：把数据从数据库或其它数据源导入到solr。
    - `extraction`：整合了[Apache Tika](http://tika.apache.org/)，Tika是用于解析一些富文本(诸如Word，PDF)的框架。
    - `langid`：检测将要索引的数据的语言。
    - `ltr`: 包含将机器学习的排名模块插入Solr的逻辑。
    - `prometheus-exporter`: Prometheus导出器，用于使用Metrics API和Search API公开Solr中的指标。
    - `velocity`：包含基于Velocity模板的简单的搜索UI框架。
- `dist`：包含Solr主要的jar文件。
- `docs`：文档。
- `example`：包含一些展示solr功能的样例。
    - `exampledocs`：这是一系列简单的CSV，XML和JSON文件，可以用`bin/post`在首次使用Solr时使用
    - `example-DIH`：此目录包含一些Data Import Handler（DIH）示例，可帮助你开始在数据库，电子邮件服务器甚至Atom订阅源中导入结构化内容。每个示例将索引不同的数据集。
    - `files`：该files目录为你可能在本地存储的文档（例如Word或PDF）提供基本的搜索UI。
    - `films`：该films目录包含一组关于电影的强大数据。
- `licenses`：包含所有的solr所用到的第三方库的许可证。
- `server`：solr应用程序的核心，包含了运行Solr实例而安装好的Jetty servlet容器。
    - `contexts`：这个文件包含了solr Web应用程序的Jetty Web应用的部署的配置文件。
    - `etc`：主要就是一些Jetty的配置文件和示例SSL密钥库。
    - `lib`：Jetty和其他第三方的jar包。
    - `logs`：Solr的日志文件。
    - `resources`：Jetty-logging和log4j的属性配置文件。
    - `solr`：新建的core或Collection的默认保存目录，里面必须要包含solr.xml文件。
    - `configsets`：包含solr的配置文件。
    - `solr-webapp`：包含solr服务器使用的文件，不要在此目录中编辑文件。

## 基本名词释义

- 倒排索引：根据属性的值来查找记录。这种索引表中的每一项都包括一个属性值和具有该属性值的各记录的地址。
- 词项频次：特定词项在待匹配文档中的出现次数，表示了文档与该词项的匹配程度。
- 反向文档频次：查询词项罕见程度的度量，根据文档频次（含该查询词项的总文档数）计算它的逆。[^frequency]
- 词项权重：特定词项或字段的相对重要性。
- 文档权重：特定文档的相对重要性。
- 字段规范因子：以每个文档为基础的特定字段重要性的因子组合。
- 长度归一化：调整不同长度的文档，通常特定词项在长文档中出现的次数可能较多，通过归一化消除较长文档的这种优势。
- 协调因子：衡量每个文档匹配的查询数量，包含更多查询词项的文档应该比只包含几个查询词项的其他文档得分更高。
- 查准率：正确匹配的文档数量/返回的文档数量。
- 查全率：正确匹配的文档数量/(正确匹配的文档数量+错误匹配的文档数量)。[^rate]
- 非规范化文档：指文档中所有字段都是自包含的，允许这些字段的值在多个文档中重复出现。

- `solr.xml`：定义管理、日志、分片、云的有关属性。
- `solrconfig.xml`：定义solr内核的主要配置。
- `schema.xml`：定义索引结构，包括字段及其数据类型。

- 6个内置搜索组件：查询（默认启用，其他几个需要在查询参数中指定相应参数启用）->分面->更多类似结果->高亮->统计->调试。
- 搜索器：任何时候只存在一个“处于活跃状态”的搜索器。提交操作会创建新的搜索器，以确保更新后的文档和索引可以被检索，solr会等到正在处理的请求被旧搜索器处理完毕后执行销毁操作，为了解决新搜索器创建时大的资源消耗导致用户体验变差，引入预热机制。
- 预热机制：一种是利用旧缓存自动预热新缓存，另一种是执行缓存预热查询。
- 缓存预热查询：向搜索器提交一段预先在`solrconfig.xml`文件中配置好的查询语句，目的是让新搜索器将需要缓存的查询结果载入它的缓存中。
- 预热新搜索器/第一个搜索器：`useColdSearcher`为`false`，solr处于阻塞状态直到所有的预热查询执行完毕；为`true`，不关心预热程度立即使用一个正在预热的搜索器进入活跃状态。`maxWarmingSearchers`允许开发者控制后台并发预热的搜索器的最大数目，一旦达到阈值，新的提交请求将失败。

- LRU：最久未使用置换法。在缓存大小达到阈值上限时，根据缓存对象最后一次被请求的时间决定缓存对象被回收的次序。
- LFU：最近最少使用置换法。根据缓存对象被请求频率的高低决定缓存对象被回收的次序。
- 缓存命中率：应用程序缓存命中的用户请求数量占所有用户请求数量的比例。
- 缓存回收数：有多少缓存对象根据缓存置换法被回收了。高缓存回收数往往会导致一个较好的缓存命中率。
- 过滤器缓存：计算并缓存合适的数据结构，找到索引中符合过滤条件的文档。
- 查询结果缓存：将查询请求的结果集保存在缓存中。solr中背后原理是将查询语句作为键，将内部lucene文档id作为值，存储在查询结果缓存中。内部lucene文档id会随搜索器改变而改变，所以在预热查询结果缓存时，缓存内部lucene文档id会重新计算。
- 文档缓存：以文档内部的id为键，将硬盘中的文档内容加载到缓存中。如果索引更新频率很低，才有助于提高性能。
- 字段值缓存：通过文档内部id快速访问存储字段值。主要用在排序和从匹配的文档中生成响应内容。

- 索引字段：询问典型用户是否能使用该字段构造有意义的查询表达式 / 如果搜索表单没有提供该字段的查询选项，但用户会提及它，那么该字段就应该被索引。
- 存储字段：文档包含一些对搜索无用的字段，但这些字段会显示在搜索结果中。
- 字段基数：字段唯一值的数量。
- 硬提交：将所有未提交的文档写入磁盘，并刷新一个内部搜索器组件，让新提交的文档能够被搜索。
- 软提交：将未提交的文档暂存在内存，在检索时同时去索引文件和内存中查询，支持近实时搜索，在某一时刻执行硬提交，以确保文档最终刷出到磁盘中。
- 自动提交：在指定的时间提交文档 / 一旦达到用户指定的未提交阈值，就提交那些未提交的文档 / 每隔特定的时间间隔提交所有的文档。
- 原子提交：对需要修改的字段进行更新。solr实际执行操作是：无论更新的是单个还是所有字段，都会删除原文档并创建新文档。但对客户端代码透明。
- 停用词：常见词汇。

- 局部参数：为特定上下文提供定制化请求参数。以`{!`开头，以`}`结尾，包含以空格分割的键值对列表，其中键值对以`=`号分割，局部参数值可能包含特殊字符，因此需要用单引号或双引号括起来，或需要对特殊字符转义。
- 参数解引用：提供查询中任意变量的替换方法。例：`/select?q={!edismax v=$userQuery}&userQuery="hello world"`。
- 字段搜索：语法为字段名称加该字段的搜索表达式，中间用冒号分割。例：title:solr。若要在同一个字段中搜索多个词项，使用组合表达式。例：`title:(apache solr)`。
- 必备词项：为指定一个或多个词项必须出现，使用一元运算符 `+` 来连接词项。若文档必须包含多个词项，使用二元运算符 `AND` 或 `&&`，或者对每个词项都使用 `+`。例：`apache AND solr` 等同 `+apache +solr`。
- 可选词项：匹配的文档至少包括某个词项，使用二元运算符 `OR` 或 `||`。例：`apache OR solr`。
- 排除词项：匹配的文档不包括某个词项，使用一元运算符 `-` 或在表达式间使用 `NOT` 布尔运算符来排除词项。例：`solr -panel`。
- 短语搜索：匹配彼此相邻的多个词项，需使用引号来定义短语范围。例：`title:"apache solr"`。
- 组合表达式：处理任意复杂的布尔子句，使用小括号将查询表达式组合在一起。例：`title:(apache solr)`。
- 权重表达式：可调整词项、短语或组合表达式的相关度权重。语法为表达式加插入号`^`加权重。例：`(apache^10 solr^100)^10 AND (apache lucene^2.5)`。
- 词项邻近搜索：通过添加波浪线`~`和词项位置的距离数，搜索位置相近的词项，不一定是彼此相邻的。例：`"apache solr"~0`。
- 字符邻近搜索：对词项中的字符进行基于编辑距离的搜索，找到拼写相似的词项。例：`solr~1`。
- 区间搜索：匹配出值的整个区间内的文档。语法为字段名加冒号加方括号（闭区间搜索）/大括号（开区间搜索），若未指定区间的最大值或最小值，可用通配符`*`占位。例：`number:[* TO 80}`。
- 通配符搜索：匹配以特定字符集开头的文档或替代单个字符的操作。语法为你要查找的文本加`*`（单或多个字符）或`?`（单个字符）。例：`hell* w?rld`。

[^frequency]: 词项频次奖励在一个文档中出现多次的词项，反文档频次惩罚在多个文档中普遍出现的词项。
[^rate]: solr在整个结果集上计算查全率，仅在搜索结果第一页（或少数页）计算查准率。在法律搜索中查全率的重要性更高。

# 快速开始

## 第一个插件

在本节中，我们将构建一个简单的插件，其中一个目标不带参数，只是在运行时在屏幕上显示一条消息。在此过程中，我们将介绍设置项目以创建插件的基础知识，定义目标代码的`Java mojo`的最小内容，以及执行`mojo`的几种方法。

### 第一个Mojo

最简单的是，`Java mojo`只包含一个代表一个插件目标的类。不需要像`EJB`这样的多个类，尽管包含许多类似`mojos`的插件可能会使用抽象超类来为`mojos`合并所有`mojos`共有的代码。

在处理源码树以查找`mojos`时，`plugin-tools`会查找带有`@Mojo` Java 5注释或`goal`javadoc注释的类。具有此批注的任何类都包含在插件配置文件中。

#### 一个简单的Mojo

下面列出的是一个没有参数的简单`mojo`类。下面是对源的各个部分的描述。

```java
package sample.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Says "Hi" to the user.
 *
 */
@Mojo( name = "sayhi")
public class GreetingMojo extends AbstractMojo
{
    public void execute() throws MojoExecutionException
    {
        getLog().info( "Hello, world." );
    }
}
```

- 除了`execute`方法之外，`org.apache.maven.plugin.AbstractMojo`类提供了实现`mojo`所需的大部分基础结构。
- 注释`@Mojo`是必需的，并控制执行`mojo`的方式和时间。
- `execute`方法可以抛出两个异常：
    - 如果发生**意外问题**抛出`org.apache.maven.plugin.MojoExecutionException`异常会导致显示`BUILD ERROR`消息。
    - 如果发生**预期问题**（例如编译失败），抛出`org.apache.maven.plugin.MojoFailureException`异常会导致显示`BUILD FAILURE`消息。
- `getLog`方法（在`AbstractMojo`中定义）返回一个类似`log4j`的`logger`对象，它允许插件以`debug`，`info`，`warn`和`error`的级别创建消息。该记录器被用于向用户显示信息。可查看检索[Mojo Logger](https://maven.apache.org/plugin-developers/common-bugs.html#Retrieving_the_Mojo_Logger)部分了解其正确用法。

所有`Mojo`注解都由[Mojo API规范](https://maven.apache.org/developers/mojo-api-specification.html#The_Descriptor_and_Annotations)描述。

### 项目定义

一旦为插件编写了`mojos`，就可以构建插件了。要正确执行此操作，项目的描述符需要正确设置许多设置：

-----|-----
`groupId` | 这是插件的组ID，应该与`mojos`使用的包的公共前缀匹配
`artifactId` | 这是插件的名称
`version` | 这是插件的版本
`packaging` | 这应该设置为`maven-plugin`
`dependencies` | 必须声明`Maven Plugin Tools API`依赖项才能解析`AbstractMojo`及相关类

下面列出了示例`mojo`项目的`pom`的示意图，其参数设置如上表所述：

```xml
<project>
  <modelVersion>4.0.0</modelVersion>

  <groupId>sample.plugin</groupId>
  <artifactId>hello-maven-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>maven-plugin</packaging>

  <name>Sample Parameter-less Maven Plugin</name>

  <dependencies>
    <dependency>
      <groupId>org.apache.maven</groupId>
      <artifactId>maven-plugin-api</artifactId>
      <version>3.0</version>
    </dependency>

    <!-- dependencies to annotations -->
    <dependency>
      <groupId>org.apache.maven.plugin-tools</groupId>
      <artifactId>maven-plugin-annotations</artifactId>
      <version>3.4</version>
      <scope>provided</scope>
    </dependency>
  </dependencies>
</project>
```

### 构建插件

使用`maven-plugin`packaging定义的标准构建生命周期中一些插件目标：

---|-----
`compile | 编译插件的`Java`代码
`process-classes | 提取数据以构建[插件描述符](https://maven.apache.org/ref/current/maven-plugin-api/plugin.html)
`test | 运行插件的单元测试
`package` | 构建插件`jar`
`install` | 在本地存储库中安装插件`jar`
`deploy` | 将插件`jar`部署到远程存储库

有关更多详细信息，您可以查看[`maven-plugin`打包的详细绑定](https://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_maven-plugin_packaging)。

### 执行你的第一个Mojo

执行新插件的最直接方法是直接在命令行上指定插件`goal`。为此，您需要在项目中配置`hello-maven-plugin`插件：

```xml
...
  <build>
    <plugins>
      <plugin>
        <groupId>sample.plugin</groupId>
        <artifactId>hello-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
      </plugin>
    </plugins>
  </build>
...
```

而且，您需要以下列形式指定完全合格的`goal`：

```shell
mvn groupId:artifactId:version:goal
```

例如，要在示例插件中运行简单的`mojo`，您可以在命令行中输入`mvn sample.plugin:hello-maven-plugin:1.0-SNAPSHOT:sayhi`。

**提示**：运行独立`goal`不需要指定版本。

#### 短命令模式

有几种方法可以减少命令所需的输入量：

- 如果需要运行本地存储库中安装最新版本的插件，则可以省略其版本号。所以只需使用`mvn sample.plugin:hello-maven-plugin:sayhi`来运行你的插件。
- 您可以为插件指定一个缩短的前缀，例如`mvn hello:sayhi`。如果您遵循使用`${prefix}-maven-plugin` 或 `maven-${prefix}-plugin`（插件是`Apache Maven`项目的一部分）的约定，则会自动完成此操作。您还可以通过其他配置分配一个特定前缀 - 有关更多信息，请参阅[插件前缀映射简介](https://maven.apache.org/guides/introduction/introduction-to-plugin-prefix-mapping.html)。
- 最后，您还可以将插件的`groupId`添加到默认搜索的`groupId`列表中。为此，您需要将以下内容添加到`${user.home}/.m2/settings.xml`文件中:

    ```xml
    <pluginGroups>
      <pluginGroup>sample.plugin</pluginGroup>
    </pluginGroups>
    ```

#### 将Mojo附加到构建生命周期

您还可以配置插件以将特定`goal`附加到构建生命周期的特定`phase`。这是一个例子：

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>sample.plugin</groupId>
        <artifactId>hello-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <executions>
          <execution>
            <phase>compile</phase>
            <goals>
              <goal>sayhi</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```

这导致在编译Java代码时执行简单的`mojo`。有关将`mojo`绑定到生命周期中的阶段的更多信息，请参阅[Build Lifecycle文档](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)。

## Mojo archetype

要创建新的插件项目，可以使用`Mojo`[archetype](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html)和以下命令行：

```shell
mvn archetype:generate \
  -DgroupId=sample.plugin \
  -DartifactId=hello-maven-plugin \
  -DarchetypeGroupId=org.apache.maven.archetypes \
  -DarchetypeArtifactId=maven-archetype-plugin
```

### 参数

没有参数，mojo不太可能非常有用。参数提供了一些非常重要的功能：

- 它提供了钩子，允许用户调整插件的操作以满足他们的需要。
- 它提供了一种从`POM`轻松提取元素值的方法，而无需导航对象。

#### 在Mojo中定义参数

定义参数就像在`mojo`中创建实例变量并添加适当的注释一样简单。下面列出的是简单`mojo`的参数示例：

```java
    /**
     * The greeting to display.
     */
    @Parameter(property = "sayhi.greeting", defaultValue = "Hello World!")
    private String greeting;
```

注释之前的部分是参数的描述。参数注解将变量标识为`mojo`参数，注解的`defaultValue`参数定义变量的默认值。该值可以包括引用项目的表达式，例如`${project.version}`（更多表达式可以在[“参数表达式”](https://maven.apache.org/ref/current/maven-core/apidocs/org/apache/maven/plugin/PluginParameterExpressionEvaluator.html)文档中找到）。`property`参数可用于允许通过引用用户通过`-D`选项设置的系统属性从命令行配置`mojo`参数。

#### 配置项目中的参数

配置插件的参数值是在`Maven`项目中的`pom.xml`文件中完成的，作为在项目中定义插件的一部分。配置插件的示例：

```xml
<plugin>
  <groupId>sample.plugin</groupId>
  <artifactId>hello-maven-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
  <configuration>
    <greeting>Welcome</greeting>
  </configuration>
</plugin>
```

在配置部分中，元素名称（`greeting`）是参数名称，元素的内容（`Welcome`）是要分配给参数的值。

**注意**：可以在[配置插件指南](https://maven.apache.org/guides/mini/guide-configuring-plugins.html)中找到更多详细信息。

#### 具有一个值的参数类型

下面列出了各种类型的简单变量，它们可以用作`mojos`中的参数，以及有关如何解释`POM`中的值的任何规则。

##### 布尔值

这包括变量类型`boolean`和`Boolean`。读取配置时，文本`true`会将参数设置为`true`，而所有其他文本都会将参数设置为`false`。例：

```java
    /**
     * My boolean.
     */
    @Parameter
    private boolean myBoolean;
```

```xml
<myBoolean>true</myBoolean>
```

##### 定点数

这包括变量类型`byte`, `Byte`, `int`, `Integer`, `long`, `Long`, `short`和`Short`。读取配置时，使用`Integer.parseInt()`或相应类的`valueOf()`方法将`XML`文件中的文本转换为整数值。这意味着字符串必须是有效的十进制整数值，仅包含数字`0`到`9`，前面带有可选项`-`则是负值。例：

```java
    /**
     * My Integer.
     */
    @Parameter
    private Integer myInteger;
```

```xml
<myInteger>10</myInteger>
```

##### 日期

这包括变量类型`Date`。在阅读配置时，`XML`文件中的文本将使用以下日期格式之一进行转换：`yyyy-MM-dd HH:mm:ss.S a`（样本日期为`2005-10-06 2:22:55.1 PM`）或`yyyy-MM-dd HH:mm:ssa`（样本日期为`2005-10-06 2:22:55PM`）。请**注意**，解析是使用`DateFormat.parse()`完成的，这允许在格式化方面有所宽容。如果该方法可以解析指定的日期和时间，即使它与上述模式不完全匹配，也可以。例：

```java
    /**
     * My Date.
     */
    @Parameter
    private Date myDate;
```

```xml
<myDate>2005-10-06 2:22:55.1 PM</myDate>
```

##### 文件和目录

这包括变量类型`File`。读取配置时，`XML`文件中的文本将用作所需文件或目录的路径。如果路径是相对的（不是以`/`或类似`C:`)的驱动器号开头，则路径相对于包含`POM`的目录。例：

```java
    /**
     * My File.
     */
    @Parameter
    private File myFile;
```

```xml
<myFile>c:\temp</myFile>
```

##### URL

这包括变量类型的`URL`。读取配置时，`XML`文件中的文本将用作`URL`。格式必须遵循`RFC 2396`指南，并且看起来像任何`Web`浏览器`URL`（`scheme://host:port/path/to/file`）。转换`URL`时，对`URL`的任何部分的内容没有任何限制。

```java
    /**
     * My URL.
     */
    @Parameter
    private URL myURL;
```

```xml
<myURL>http://maven.apache.org</myURL>
```

##### 纯文本

这包括变量类型`char`，`Character`，`StringBuffer`和`String`。读取配置时，`XML`文件中的文本将用作要分配给参数的值。对于`char`和`Character`参数，仅使用文本的第一个字符。

##### 枚举

也可以使用枚举类型参数。首先，您需要定义枚举类型，然后可以在参数定义中使用枚举类型：

```java
    public enum Color {
      GREEN,
      RED,
      BLUE
    }

    /**
     * My Enum
     */
    @Parameter
    private Color myColor;
```

你可以在你的`pom`配置中这样使用枚举：

```xml
<myColor>GREEN</myColor>
```

您还可以使用枚举类型中的元素作为`defaultValues`，如下所示：

```java
    public enum Color {
      GREEN,
      RED,
      BLUE
    }

    /**
     * My Enum
     */
    @Parameter(defaultValue = "GREEN")
    private Color myColor;
```

#### 具有多个值的参数类型

下面列出了各种类型的复合对象，它们可以用作`mojos`中的参数，以及有关如何解释`POM`中的值的任何规则。通常，为保存参数值而创建的对象的类（以及参数值中每个元素的类）的确定如下（产生有效类的第一步）：

1. 如果`XML`元素包含`implementation`提示属性，则使用该属性
2. 如果`XML`标记包含`.`，请将其作为完全限定的类名称
3. 尝试将`XML`标记（带大写的第一个字母）作为与正在配置的`mojo/object`相同的包中的类
4. 对于数组，请使用数组的组件类型（例如，对`String []`参数使用`String`）;对于集合和映射，使用`mojo`配置中为集合或映射指定的类;将`String`用于集合中的条目和映射中的值

一旦定义了元素的类型，`XML`文件中的文本就会转换为适当类型的对象

##### 数组

通过多次指定参数来配置数组类型参数。例：

```java
    /**
     * My Array.
     */
    @Parameter
    private String[] myArray;
```

```xml
<myArray>
  <param>value1</param>
  <param>value2</param>
</myArray>
```

##### 集合

此类别包含实现`java.util.Collection`的任何类，例如`ArrayList`或`HashSet`。通过像数组一样多次指定参数来配置这些参数。例：

```java
    /**
     * My List.
     */
    @Parameter
    private List myList;
```

```xml
<myList>
  <param>value1</param>
  <param>value2</param>
</myList>
```

有关各个集合元素的映射的详细信息，请参阅[映射列表](https://maven.apache.org/guides/mini/guide-configuring-plugins.html#Mapping_Lists)。

##### 映射

此类别涵盖实现`java.util.Map`的任何类，例如`HashMap`，但不实现`java.util.Properties`。通过在参数配置中以`<key>value</key>`的形式来配置这些参数。例：

```java
    /**
     * My Map.
     */
    @Parameter
    private Map myMap;
```

```xml
<myMap>
  <key1>value1</key1>
  <key2>value2</key2>
</myMap>
```

##### Properties

此类别包含实现`java.util.Properties`的任何映射。通过在参数配置中以`<property> <name>myName</name> <value>myValue</value> </property>`的形式来配置这些参数。例：

```java
    /**
     * My Properties.
     */
    @Parameter
    private Properties myProperties;
```

```xml
<myProperties>
  <property>
    <name>propertyName1</name>
    <value>propertyValue1</value>
  <property>
  <property>
    <name>propertyName2</name>
    <value>propertyValue2</value>
  <property>
</myProperties>
```

##### 其他对象类

此类别包含未实现`java.util.Map`，`java.util.Collection`或`java.util.Dictionary`的任何类。

```java
    /**
     * My Object.
     */
    @Parameter
    private MyObject myObject;
```

```xml
<myObject>
  <myField>test</myField>
</myObject>
```

## 使用Setters

您不限于使用私有字段映射，如果您试图让`Mojos`在`Maven`的上下文之外也可以被重复使用，那么这是很好的。对于上面的示例，我们可以使用**下划线**约定命名我们的私有字段，并提供可使用的setter配置映射机制。所以我们的`Mojo`看起来如下示：

```java
public class MyQueryMojo extends AbstractMojo {
    @Parameter(property="url")
    private String _url;

    @Parameter(property="timeout")
    private int _timeout;

    @Parameter(property="options")
    private String[] _options;

    public void setUrl(String url) {
        _url = url;
    }

    public void setTimeout(int timeout) {
        _timeout = timeout;
    }

    public void setOptions(String[] options) {
        _options = options;
    }

    public void execute() throws MojoExecutionException {
        ...
    }
}
```

请注意每个参数的属性名称的规范，该参数告诉`Maven`当字段的名称与插件配置中的参数的预期名称不匹配时要使用的`setter`和`getter`。

## 其它资源

- [Mojo Documentation](https://maven.apache.org/developers/mojo-api-specification.html): `Mojo API`, `Mojo`注解。
- [Maven Plugin Testing Harness](https://maven.apache.org/shared/maven-plugin-testing-harness/): 测试`Mojos`的框架。
- [Plexus](https://codehaus-plexus.github.io/): `Maven`使用的`IoC`容器。
- [Plexus Common Utilities](https://codehaus-plexus.github.io/plexus-utils/): 用于`Mojo`开发的一组实用程序类。
- [Commons IO](http://commons.apache.org/io/): 用于文件/路径处理的实用程序类集。
- [Common Bugs and Pitfalls](https://maven.apache.org/plugin-developers/common-bugs.html): 有问题的编码模式概述。

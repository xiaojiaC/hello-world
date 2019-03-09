# Groovy与Java的差异

Groovy试图让Java开发人员尽可能自然。在设计Groovy时，我们已经尝试遵循最小惊喜的原则，特别是对于那些学习来自Java背景的Groovy的开发人员。

## 默认导入

默认情况下导入所有下面这些包和类，你不必使用显式使用`import`语句来导入它们：

- `java.io.*`
- `java.lang.*`
- `java.math.BigDecimal`
- `java.math.BigInteger`
- `java.net.*`
- `java.util.*`
- `groovy.lang.*`
- `groovy.util.*`

## 多方法

在Groovy中，将在运行时选择将调用的方法。这称为运行时调度或多方法。这意味着将根据运行时参数的类型选择方法。在Java中，则恰恰相反：在编译时根据声明的类型选择方法。

下面的Java代码，可以在Java和Groovy中编译，但它的行为会有所不同：

```groovy
int method(String arg) {
    return 1;
}
int method(Object arg) {
    return 2;
}
Object o = "Object";
int result = method(o);
```

在Java中：

```java
assertEquals(2, result);
```

在Groovy中：

```groovy
assertEquals(1, result);
```

这是因为Java将使用静态信息类型，即`o`被声明为`Object`，而Groovy将在运行时选择实际调用方法，由于它是用`String`调用的，因此调用`String`版本的方法。

## 数组初始化

在Groovy中，`{...}`块保留用于闭包。这意味着你无法使用以下语法创建数组字面量：

```java
int[] array = { 1, 2, 3 }
```

你必须使用：

```groovy
int[] array = [1,2,3]
```

## 包访问权限

在Groovy中，省略字段上的修饰符不会导致像Java那样默认为包可见：

```groovy
class Person {
    String name
}
```

相反为私有字段，但会创建属性相关的*getter*和*setter*方法。

可以通过使用`@PackageScope`注解来创建包可见字段：

```groovy
class Person {
    @PackageScope String name
}
```

## ARM块

Groovy不支持Java 7中的ARM（Automatic Resource Management/自动资源管理）块。相反，Groovy提供了依赖于闭包的各种方法，这些方法具有相同的效果且更简单。例如：

```java
Path file = Paths.get("/path/to/file");
Charset charset = Charset.forName("UTF-8");
try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }

} catch (IOException e) {
    e.printStackTrace();
}
```

可以像这样写：

```groovy
new File('/path/to/file').eachLine('UTF-8') {
   println it
}
```

或者，如果你想要一个更接近Java的版本：

```groovy
new File('/path/to/file').withReader('UTF-8') { reader ->
   reader.eachLine {
       println it
   }
}
```

## 内部类

匿名内部类和嵌套类的实现遵循Java主导，但是你不应该删除Java语言规范并且不断地讨论不同的事情。现有实现看起来很像我们为`groovy.lang.Closure`做的，有一些好处和一些差异。例如，访问私有字段和方法可能会成为一个问题，但另一方面，局部变量并不一定是`final`类型的。

### 静态内部类

这是静态内部类的一个例子：

```java
class A {
    static class B {}
}

new A.B()
```

静态内部类的使用支持最好。如果你一定需要一个内部类，将其声明为静态类应该是你的首选。

### 匿名内部类

```groovy
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

CountDownLatch called = new CountDownLatch(1)

Timer timer = new Timer()
timer.schedule(new TimerTask() {
    void run() {
        called.countDown()
    }
}, 0)

assert called.await(10, TimeUnit.SECONDS)
```

### 创建非静态内部类的实例

在Java中，你可以这样做：

```java
public class Y {
    public class X {}
    public X foo() {
        return new X();
    }
    public static X createX(Y y) {
        return y.new X();
    }
}
```

Groovy不支持`y.new X()`语法。相反，你必须写做`new X(y)`，如下面的代码所示：

```groovy
public class Y {
    public class X {}
    public X foo() {
        return new X()
    }
    public static X createX(Y y) {
        return new X(y)
    }
}
```

但是请注意，Groovy支持使用一个参数调用方法而不提供参数，则该参数的值为null。基本上相同的规则适用于调用构造函数。例如，你可能会编写`new X()`而不是`new X(this)`。由于这也可能是常规的方法，我们尚未找到防止此问题的好方法。

## Lambdas表达式

Java 8支持lambda和方法引用：

```java
Runnable run = () -> System.out.println("Run");
list.forEach(System.out::println);
```

Java 8 lambdas可以或多或少地被视为匿名内部类。Groovy不支持该语法，但有闭包：

```groovy
Runnable run = { println 'run' }
list.each { println it } // or list.each(this.&println)
```

## GStrings

由于双引号字符串文字被解释为`GString`值，如果使用Groovy和Java编译器编译包含美元字符的`String`文本类，Groovy可能会因编译错误而失败或者生成稍微不同的代码。

虽然通常情况下，如果API声明了参数的类型，Groovy会在`GString`和`String`之间自动转换，但是要小心接收`Object`参数的Java API，并检查实际类型。

## String和Character文字

Groovy中单引号文字会用`String`；而双引号文字会用`String`或`GString`，具体取决于文字中是否有插值。

```groovy
assert 'c'.getClass()==String
assert "c".getClass()==String
assert "c${1}".getClass() in GString
```

只有在赋值给`char`类型的变量时，Groovy才会自动将单字符`String`转换为`char`。当使用`char`类型的参数调用方法时，我们需要显式转换或确保已提前转换成了`char`值。

```groovy
char a='a'
assert Character.digit(a, 16)==10 : 'But Groovy does boxing'
assert Character.digit((char) 'a', 16)==10

try {
  assert Character.digit('a', 16)==10
  assert false: 'Need explicit cast'
} catch(MissingMethodException e) {
}
```

Groovy支持两种类型的转换，在将类型转换为`char`的情况下，在转换多字符字符串时存在细微的差别。Groovy样式转换更宽松，将采用第一个字符，而C样式转换将失败，抛出异常。

```groovy
// 对于单字符字符串，两者都是相同的
assert ((char) "c").class==Character
assert ("c" as char).class==Character

// 对于多字符字符串，它们不是
try {
  ((char) 'cx') == 'c'
  assert false: 'will fail - not castable'
} catch(GroovyCastException e) {
}
assert ('cx' as char) == 'c'
assert 'cx'.asType(char) == 'c'
```

## 元和包装类型

因为Groovy使用Objects来实现一切，所以所有基本类型都会[自动转换](http://docs.groovy-lang.org/latest/html/documentation/core-object-orientation.html#_primitive_types)为其包装类型来使用。Groovy不支持Java的*扩展优先于包装*的规则。这是一个使用`int`的例子：

```groovy
int i
m(i)

void m(long l) {           // <1>
  println "in m(long)"
}

void m(Integer i) {        // <2>
  println "in m(Integer)"
}
```

&lt;1&gt;: 这是Java可以调用的方法，因为扩展优先于拆箱。

&lt;2&gt;: 这是Groovy实际调用的方法，因为所有原始引用都使用它们的包装类。

## ==的行为

在Java中`==`表示基本类型或对象引用的相等性。在Groovy中，如果它们是`Comparable`则`==`转换为`a.compareTo(b)==0`，否则转换为`a.equals(b)`。如果需要判断对象引用相等性，可以使用`is`函数，例如：`a.is(b)`。

## 转换

Java会自动扩展和缩小[转换](https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html)次数。

Java转换：

|     | 转换为   |       |       |    |      |      |       |     |
|:-----|:------:|:-----:|:-----:|:---:|:---:|:----:|:-----:|:----:|
| 转换自 | boolean | byte | short | char | int | long | float | double |
| boolean | \-    |  N   | N     | N    | N   | N    | N     | N  |
| byte |    N     |  \-  | Y     | C    | Y   | Y    | Y     | Y  |
| short |   N     |  C   | \-    | C    | Y   | Y    | Y     | Y  |
| char |    N     |  C   | C     | \-   | Y   | Y    | Y     | Y  |
| int |     N     |  C   | C     | C    | \-  | Y    | T     | Y  |
| long |    N     |  C   | C     | C    | C   | \-   | T     | T  |
| float |   N     |  C   | C     | C    | C   | C    | \-    | Y  |
| double |  N     |  C   | C     | C    | C   | C    | C     | \- |

'Y'：表示Java可以进行的转换 <br/>
'C'：表示Java在有显式强制转换时可以进行的转换 <br/>
'T'：表示Java可以进行的转换但数据被截断 <br/>
'N'：表示Java无法进行的转换 <br/>

Groovy转换：

|     | 转换为|       |      |    |     |     |    |       |    |       |    |    |          |    |     |      |       |      |
|:----|:-----:|:-----:|:---:|:--:|:---:|:---:|:--:|:------:|:--:|:-----:|:--:|:--:|:--------:|:--:|:---:|:----:|:-----:|:----:|
| 转换自 | boolean | Boolean | byte | Byte | short | Short | char | Character | int | Integer | long | Long | BigInteger | float | Float | double | Double | BigDecimal
| boolean |   \- |   B    | N  | N  |  N  |  N  |  N |    N    |  N |  N    |  N | N  |     N    |  N  |  N  |  N   |  N   |    N |
| Boolean |   B  |   \-   | N  | N  |  N  |  N  |  N |    N    |  N |  N    |  N | N  |     N    |  N  |  N  |  N   |  N   |    N |
| byte |      T  |   T    | \- | B  |  Y  |  Y  |  Y |    D    |  Y |  Y    |  Y | Y  |     Y    |  Y  |  Y  |  Y   |  Y   |    Y |
| Byte |      T  |   T    | B  | \- |  Y  |  Y  |  Y |    D    |  Y |  Y    |  Y | Y  |     Y    |  Y  |  Y  |  Y   |  Y   |    Y |
| short |     T  |   T    | D  | D  |  \- |  B  |  Y |    D    |  Y |  Y    |  Y | Y  |     Y    |  Y  |  Y  |  Y   |  Y   |    Y |
| Short |     T  |   T    | D  | T  |     |  \- |  Y |    D    |  Y |  Y    |  Y | Y  |     Y    |  Y  |  Y  |  Y   |  Y   |    Y |
| char |      T  |   T    | Y  | D  |     |     | \- |    D    |  Y |  D    |  Y | D  |     D    |  Y  |  D  |  Y   |  D   |    D |
| Character | T  |   T    | D  | D  |  D  |  D  |  D |    \-   |  D |  D    |  D | D  |     D    |  D  |  D  |  D   |  D   |    D |
| int |       T  |   T    | D  | D  |  D  |  D  |  Y |    D    | \- |  B    |  Y | Y  |     Y    |  Y  |  Y  |  Y   |  Y   |    Y |
| Integer |   T  |   T    | D  | D  |  D  |  D  |  Y |    D    |  B |  \-   |  Y | Y  |     Y    |  Y  |  Y  |  Y   |  Y   |    Y |
| long |      T  |   T    | D  | D  |  D  |  D  |  Y |    D    |  D |  D    | \- | B  |     Y    |  T  |  T  |  T   |  T   |    Y |
| Long |      T  |   T    | D  | D  |  D  |  T  |  Y |    D    |  D |  T    |  B | \- |     Y    |  T  |  T  |  T   |  T   |    Y |
| BigInteger | T |   T    | D  | D  |  D  |  D  |  D |    D    |  D |  D    |  D | D  |     \-   |  D  |  D  |  D   |  D   |    T |
| float |     T  |   T    | D  | D  |  D  |  T  |  D |    D    |  D |  D    |  D | D  |     D    |  \- |  B  |  Y   |  Y   |    Y |
| Float |     T  |   T    | D  | T  |  D  |  T  |  T |    D    |  D |  T    |  D | T  |     D    |  B  |  \- |  Y   |  Y   |    Y |
| double |    T  |   T    | D  | D  |  D  |  D  |  T |    D    |  D |  D    |  D | D  |     D    |  D  |  D  |  \-  |  B   |    Y |
| Double |    T  |   T    | D  | T  |  D  |  T  |  T |    D    |  D |  T    |  D | T  |     D    |  D  |  T  |  B   |  \-  |    Y |
| BigDecimal | T |   T    | D  | D  |  D  |  D  |  D |    D    |  D |  D    |  D | D  |     D    |  T  |  D  |  T   |  D   |   \- |

'Y'：表示Groovy可以进行的转换 <br/>
'D'：表示Groovy在动态编译或显式强制转换时可以进行的转换 <br/>
'T'：表示Groovy可以进行的转换但数据被截断 <br/>
'B'：表示装箱/拆箱操作 <br/>
'N'：表示Groovy无法进行的转换 <br/>

转换为`boolean`/`Boolean`时，截断使用[Groovy Truth](http://docs.groovy-lang.org/latest/html/documentation/core-semantics.html#Groovy-Truth)。从数字转换为字符会将`Number.intvalue()`转换为`char`。当从`Float`或`Double`转换时，Groovy使用`Number.doubleValue()`构造`BigInteger`和`BigDecimal`，否则使用`toString()`构造。其他转换的行为由`java.lang.Number`定义。

## 额外关键字

Groovy中的关键字比Java中的更多。不要将它们用于变量名称等。

- as
- def
- in
- trait

> [原文链接](http://www.groovy-lang.org/differences.html)

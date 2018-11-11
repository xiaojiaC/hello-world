## String

String是不可变,其内部的方法都很小心的没有动char[]中的元素，而且也没有暴露value成员变量。甚至整个String设成final禁止继承,避免被其他人继承后破坏。

```java
    /* 字符串是常量；它们的值在创建之后不能更改。
     * 字符串缓冲区支持可变的字符串。因为 String 对象是不可变的，所以可以共享。例如：
     *
     *   String str = "abc";
     *
     * 等效于：
     *
     *   char data[] = {'a', 'b', 'c'};
     *   String str = new String(data);
     */
    public final class String
        implements java.io.Serializable, Comparable<String>, CharSequence {
    }
```

### 成员变量

- String中的字符数组被设计成final的, 内容一旦被初始化后不能被修改。这里也只是说value的引用地址不可变，没有说堆里数组本身数据不可变。通过反射还是可以更改数组元素值。
- String重写了Object的hashCode函数使hash值基于字符数组内容，但是由于String缓存了hash值，所以即便通过反射改变了字符数组内容，hashCode返回值不会自动更新。

```java
    /**
     * 不可变的char数组用来存放Unicode字符
     *
     * Java9对String实现进行优化，使用byte[]替换char[]，这样如果都是ASCII字符，就可直接使用1个字节表示该字符而不是2字节，节约内存
     */
    private final char value[];

    /** 缓存String的hash值。以空间换时间提升性能，因为字符串的hashcode经常被用到。 */
    private int hash; // Default to 0

    /**
     * s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
     * - 可以产生更离散的散列值
     * - 可以使用更高效的移位和减法操作代替乘法操作, 31*h = 32*h - h = (h<<5) - h
     */
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }
```

> [String的hashCode函数为什么选取质数31](https://stackoverflow.com/questions/299304/why-does-javas-hashcode-in-string-use-31-as-a-multiplier)


### 静态属性

```java
    /** 使用JDK1.0.2的serialVersionUID来实现互操作性. 
     * 
     * serialVersionUID主要被用来解决类版本升级后，对象序列化向后兼容问题。
     * 该域的值一般是综合类的各个特性而计算出来的一个哈希值，可以通过Java提供的serialver命令来生成。
     * 通过在实现了Serializable接口的类中定义该域，JVM会比对从字节数组中得出的类的版本号，与JVM中
     * 查找到的类的版本号是否一致，来决定两个类是否是兼容的。*/
    private static final long serialVersionUID = -6849794470754667710L;

    /**
     * serialPersistentFields域用来声明序列化时要包含的域。
     *
     * Why: 该字段值是空数组，但是String类中的value却能被序列化？
     */
    private static final ObjectStreamField[] serialPersistentFields =
        new ObjectStreamField[0];
```

> [Java对象序列化与RMI](http://www.infoq.com/cn/articles/cf-java-object-serialization-rmi/)

### 构造函数

String主要可通过String、char[]、int[]（CodePoint）、byte[]（编码）、StringBuilder、StringBuffer进行初始化。

- 通过char数组，StringBuffer，StringBuilder进行初始化时，要执行value数组元素的拷贝，创建新数组，防止外部改变value值。
- 通过String进行初始化时,并没有执行value数组拷贝，因为源字符串value数组是不可变的，也就间接保证了新String对象的不可变性。
- 通过byte数组进行初始化时，需要指定编码或使用默认编码（ISO-8859-1），否则无法正确解码字节内容。
- 通过Unicode代码点进行的初始化时，非BMP字符需要使用两个char字符保存，这也就导致了新字符串的长度可能会大于int数组的长度。

```java

    public String() {
        this.value = new char[0];
    }

    /** 通过字符串初始化字符串时，并没有执行value数组拷贝，因为original的value数组是不可以在外部修改的，
     * 也就保证了新String对象的不可修改，即便更改你也只是改变源字符串的引用。 */
    public String(String original) {
        this.value = original.value;
        this.hash = original.hash;
    }

    public String(char value[]) {
        this.value = Arrays.copyOf(value, value.length);
    }

    /** 使用平台的默认字符集，从源数组offset位置起复制count个字符，构造一个新的String。*/
    public String(char value[], int offset, int count) {
        if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count < 0) {
            throw new StringIndexOutOfBoundsException(count);
        }
        // Note: offset or count might be near -1>>>1.
        if (offset > value.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }
        this.value = Arrays.copyOfRange(value, offset, offset+count);
    }

    /** 此方法不同于上一个方法是使用代码点来构造一个新的String。那么什么是代码点呢？首先我们来了解一下Unicode基础：
     *
     * Unicode给世界上每个字符分配了一个编号，编号范围从0x000000到0x10FFFF。编号范围在0x0000到0xFFFF之间的字符，为常用字符集，称BMP
     * (Basic Multilingual Plane)字符。编号范围在0x10000到0x10FFFF之间的字符叫做增补字符(supplementary character)。
     *
     * Unicode主要规定了编号，但没有规定如果把编号映射为二进制，UTF-16是一种编码方式，或者叫映射方式，它将编号映射为两个或四个字节，对BMP字符，
     * 它直接用两个字节表示，对于增补字符，使用四个字节，前两个字节叫高代理项(high surrogate)，范围从0xD800到0xDBFF，后两个字节叫低代理项
     * (low surrogate)，范围从0xDC00到0xDFFF，UTF-16定义了一个公式，可以将编号与四字节表示进行相互转换。
     *
     * Java内部采用UTF-16编码，char表示一个字符，但只能表示BMP中的字符，对于增补字符，需要使用两个char表示，一个表示高代理项，一个表示低代理项。
     * 使用int可以表示任意一个Unicode字符，低21位表示Unicode编号，高11位设为0。整数编号在Unicode中一般称为代码点(Code Point)，表示一个
     * Unicode字符，与之相对，还有一个词代码单元(Code Unit)表示一个char。
     */
    public String(int[] codePoints, int offset, int count) {
        if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count < 0) {
            throw new StringIndexOutOfBoundsException(count);
        }
        // Note: offset or count might be near -1>>>1.
        if (offset > codePoints.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }

        final int end = offset + count;

        // 1: 计算char[]的精确大小
        int n = count;
        for (int i = offset; i < end; i++) {
            int c = codePoints[i];
            if (Character.isBmpCodePoint(c)) // 小于等于0xFFFF的为BMP字符. 其内部算法是: codePoint >>> 16 == 0; 因为一个char占2个字节也就是16位,而BMP字符用1个char就可以表示。
                continue;
            else if (Character.isValidCodePoint(c)) // 小于等于0x10FFFF的为有效. 其内部算法原理与isBmpCodePoint类似。非BMP的有效代码点，必须用2个char字符才能完整表示。
                n++;
            else throw new IllegalArgumentException(Integer.toString(c));
        }

        // 2: 分配空间并填充char[]数组
        final char[] v = new char[n];

        for (int i = offset, j = 0; i < end; i++, j++) { // BMP字符直接存储，增补字符的用两个char分别存储高位和低位
            int c = codePoints[i];
            if (Character.isBmpCodePoint(c))
                v[j] = (char)c;
            else
                Character.toSurrogates(c, v, j++);
        }

        this.value = v;
    }

    public String(byte bytes[], int offset, int length, String charsetName)
            throws UnsupportedEncodingException {
        if (charsetName == null)
            throw new NullPointerException("charsetName");
        checkBounds(bytes, offset, length);
        this.value = StringCoding.decode(charsetName, bytes, offset, length);
    }

    public String(byte bytes[], int offset, int length, Charset charset) {
        if (charset == null)
            throw new NullPointerException("charset");
        checkBounds(bytes, offset, length);
        this.value =  StringCoding.decode(charset, bytes, offset, length);
    }

    public String(byte bytes[], int offset, int length) {
        checkBounds(bytes, offset, length);
        this.value = StringCoding.decode(bytes, offset, length);
    }

    public String(byte bytes[], String charsetName)
            throws UnsupportedEncodingException {
        this(bytes, 0, bytes.length, charsetName);
    }

    public String(byte bytes[], Charset charset) {
        this(bytes, 0, bytes.length, charset);
    }

    public String(byte bytes[]) {
        this(bytes, 0, bytes.length);
    }

    public String(StringBuffer buffer) {
        synchronized(buffer) {
            this.value = Arrays.copyOf(buffer.getValue(), buffer.length());
        }
    }

    /**
     * 通过{@code toString}方法从StringBuilder中获取字符串可能比从StringBuffer中运行得更快，通常是首选。
     * 因为相比StringBuffer而言，它的toString方法并不需要加synchronized关键字来确保线程安全。
     */
    public String(StringBuilder builder) {
        this.value = Arrays.copyOf(builder.getValue(), builder.length());
    }

    /** 包私有构造函数，可共享字符数组为了速度。 */
    String(char[] value, boolean share) {
        // assert share : "unshared not supported";
        this.value = value;
    }
```

### 成员方法

#### 比较方法

```java
    /** 与其他字符序列相等性比较　*/
    public boolean contentEquals(StringBuffer sb)

    private boolean nonSyncContentEquals(AbstractStringBuilder sb)

    public boolean contentEquals(CharSequence cs) {
        // Argument is a StringBuffer, StringBuilder
        if (cs instanceof AbstractStringBuilder) {
            if (cs instanceof StringBuffer) {
                synchronized(cs) {
                   return nonSyncContentEquals((AbstractStringBuilder)cs);
                }
            } else {
                return nonSyncContentEquals((AbstractStringBuilder)cs);
            }
        }
        // Argument is a String
        if (cs instanceof String) {
            return equals(cs);
        }
        // Argument is a generic CharSequence
        char v1[] = value;
        int n = v1.length;
        if (n != cs.length()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (v1[i] != cs.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /** 与其他字符串匹配性(大小写敏感, 区域段)比较　*/
    public boolean equalsIgnoreCase(String anotherString)

    public boolean regionMatches(int toffset, String other, int ooffset, int len)

    public boolean regionMatches(boolean ignoreCase, int toffset, String other, int ooffset, int len) {
        char ta[] = value;
        int to = toffset;
        char pa[] = other.value;
        int po = ooffset;
        // Note: toffset, ooffset, or len might be near -1>>>1.
        if ((ooffset < 0) || (toffset < 0)
                || (toffset > (long)value.length - len)
                || (ooffset > (long)other.value.length - len)) {
            return false;
        }
        while (len-- > 0) {
            char c1 = ta[to++];
            char c2 = pa[po++];
            if (c1 == c2) {
                continue;
            }
            if (ignoreCase) {
                // If characters don't match but case may be ignored,
                // try converting both characters to uppercase.
                // If the results match, then the comparison scan should
                // continue.
                char u1 = Character.toUpperCase(c1);
                char u2 = Character.toUpperCase(c2);
                if (u1 == u2) {
                    continue;
                }
                // 转换为大写格式在格鲁吉亚字母表不能正常工作，它具有有关字母转换的奇怪规则。
                if (Character.toLowerCase(u1) == Character.toLowerCase(u2)) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    /** Comparable接口比较行为的实现，实现该接口的类也意味着其"支持排序"　*/
    public int compareTo(String anotherString)

    /** 通过组合内置比较器，实现忽略大小写的比较行为　*/
    public int compareToIgnoreCase(String str) {
        return CASE_INSENSITIVE_ORDER.compare(this, str);
    }

    /**
     * 策略模式：定义算法族，分别封装起来，让他们之间可以相互替换，此模式让算法的变化独立于使用算法的客户。
     * - 分离变与不变
     * - 针对接口编程，而不是针对实现编程
     * - 多用组合，少用继承
     *
     * 不同的排序方式就是不同的策略。
     * eg: Arrays.sort(arr, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
     */
    public static final Comparator<String> CASE_INSENSITIVE_ORDER
                                             = new CaseInsensitiveComparator();
```

#### 检索索引方法

```java
    public int indexOf(int ch)

    public int indexOf(int ch, int fromIndex)

    public int lastIndexOf(int ch)

    public int lastIndexOf(int ch, int fromIndex)　{
        if (ch < Character.MIN_SUPPLEMENTARY_CODE_POINT) { // 处理大多数情况, BMP字符和无效的代码点
            final char[] value = this.value;
            int i = Math.min(fromIndex, value.length - 1);
            for (; i >= 0; i--) {
                if (value[i] == ch) {
                    return i;
                }
            }
            return -1;
        } else { // 增补字符
            return lastIndexOfSupplementary(ch, fromIndex);
        }
    }

    private int lastIndexOfSupplementary(int ch, int fromIndex) {
        if (Character.isValidCodePoint(ch)) {
            final char[] value = this.value;
            char hi = Character.highSurrogate(ch);
            char lo = Character.lowSurrogate(ch);
            int i = Math.min(fromIndex, value.length - 2);
            for (; i >= 0; i--) {
                if (value[i] == hi && value[i + 1] == lo) {
                    return i;
                }
            }
        }
        return -1;
    }
```

> [为什么可通过int检索字符索引](https://stackoverflow.com/questions/26080643/java-public-int-indexofint-ch)

#### 替换方法

```java
    public String replace(char oldChar, char newChar) {
        if (oldChar != newChar) {
            int len = value.length;
            int i = -1;
            char[] val = value; /* 避免getfield操作码. 其用于获取类的成员变量。如果不这样做，循环中每次引用value值时都必须执行getfield */

            while (++i < len) {
                if (val[i] == oldChar) { // 找出代替换的索引位置
                    break;
                }
            }
            if (i < len) {
                char buf[] = new char[len]; 
                for (int j = 0; j < i; j++) {　// 拷贝原0~索引位置的字符到新数组
                    buf[j] = val[j];
                }
                while (i < len) {
                    char c = val[i];
                    buf[i] = (c == oldChar) ? newChar : c; // 将索引位置的字符替换为新字符后，继续拷贝原后半部分字符
                    i++;
                }
                return new String(buf, true); // 使用共享字符数组，直接指向新开辟的数组地址
            }
        }
        return this;
    }

    public String replaceFirst(String regex, String replacement)

    public String replaceAll(String regex, String replacement)

    public String replace(CharSequence target, CharSequence replacement)
```

> [avoid getfield opcode](https://stackoverflow.com/questions/4761681/avoiding-getfield-opcode/)

#### Other

```java
    void getChars(char dst[], int dstBegin) {
        System.arraycopy(value, 0, dst, dstBegin, value.length);
    }

    public String concat(String str) {
        int otherLen = str.length();
        if (otherLen == 0) {
            return this;
        }
        int len = value.length;
        char buf[] = Arrays.copyOf(value, len + otherLen); // 将原字符串value数组拷贝到新开辟数组前段
        str.getChars(buf, len); // 将拼接字符串value数组拷贝到新开辟数组后段
        return new String(buf, true); // 使用共享字符数组，直接指向新开辟的数组地址，优化性能，也防止了空间浪费
    }

    public static String join(CharSequence delimiter, CharSequence... elements)

    public static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringJoiner joiner = new StringJoiner(delimiter); // 内部主要使用StringBuilder实现拼接行为
        for (CharSequence cs: elements) {
            joiner.add(cs);
        }
        return joiner.toString();
    }

    public String trim() {
        int len = value.length;
        int st = 0;
        char[] val = value;    /* avoid getfield opcode */

        while ((st < len) && (val[st] <= ' ')) {　// 找非空字符起始索引
            st++;
        }
        while ((st < len) && (val[len - 1] <= ' ')) { // 计数非空字符串长度
            len--;
        }
        return ((st > 0) || (len < value.length)) ? substring(st, len) : this;
    }

    public char[] toCharArray() {
        // 不能使用Arrays.copyOf，由于类初始化顺序的问题
        char result[] = new char[value.length];
        System.arraycopy(value, 0, result, 0, value.length);
        return result;
    }

    public static String valueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    public static String valueOf(boolean b)

    public static String valueOf(char c)

    public static String valueOf(int i)

    public static String valueOf(long l)

    public static String valueOf(float f)

    public static String valueOf(double d)　{
        return Double.toString(d);
    }

    /** 返回字符串对象的规范化表示形式。当调用该方法时，如果池已经包含一个等于此String对象的字符串（由 equals(Object) 方法确定），
    则返回池中的字符串。否则，将此String对象添加到池中，并且返回此String对象的引用。 */
    public native String intern();
```

> String可以直接使用+和+=运算符，这是编译器提供的支持，背后是通过生成StringBuilder，+和+=操作会转换成append。对于简单的情况，可以直接使用String的+和+=，对于复杂的情况，尤其是有循环的时候，应该直接手动使用StringBuilder，防止编译器优化后在循环内生成多个。

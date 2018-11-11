## AbstractStringBuilder

```java
    /** 一个可变的字符序列的抽象。 */
    abstract class AbstractStringBuilder implements Appendable, CharSequence {
    }
```

### 成员变量

```java
    /** value数组用于字符存储。*/
    char[] value;

    /** 已存储的字符数。由此可见value.length(容量)大于等于count(长度)。 
        存储该信息，减少获取已存储的字符数的时间复杂度到O(1)。*/
    int count;
```

### 构造函数

```java
    AbstractStringBuilder() {}

    AbstractStringBuilder(int capacity) {
        value = new char[capacity];
    }
```

### 成员方法

#### 追加方法

```java
    public AbstractStringBuilder append(Object obj) {
        return append(String.valueOf(obj)); // 追加的是对象，直接使用对象的toString()值
    }

    public AbstractStringBuilder append(String str) {
        if (str == null)
            return appendNull();
        int len = str.length();
        ensureCapacityInternal(count + len);
        str.getChars(0, len, value, count); // 追加的是字符串，则将字符串的字符数组拷贝到value数组尾部
        count += len;
        return this;
    }

    public AbstractStringBuilder append(StringBuffer sb)

    AbstractStringBuilder append(AbstractStringBuilder asb)

    @Override
    public AbstractStringBuilder append(CharSequence s) {
        if (s == null)
            return appendNull();
        if (s instanceof String)
            return this.append((String)s);
        if (s instanceof AbstractStringBuilder)
            return this.append((AbstractStringBuilder)s);

        return this.append(s, 0, s.length());
    }

    public AbstractStringBuilder append(CharSequence s, int start, int end)

    private AbstractStringBuilder appendNull() {
        int c = count;
        ensureCapacityInternal(c + 4);
        final char[] value = this.value;
        value[c++] = 'n';
        value[c++] = 'u';
        value[c++] = 'l';
        value[c++] = 'l';
        count = c;
        return this;
    }

    public AbstractStringBuilder append(char[] str)

    public AbstractStringBuilder append(char str[], int offset, int len)

    public AbstractStringBuilder append(boolean b) {
        if (b) {
            ensureCapacityInternal(count + 4);
            value[count++] = 't';
            value[count++] = 'r';
            value[count++] = 'u';
            value[count++] = 'e';
        } else {
            ensureCapacityInternal(count + 5);
            value[count++] = 'f';
            value[count++] = 'a';
            value[count++] = 'l';
            value[count++] = 's';
            value[count++] = 'e';
        }
        return this;
    }

    public AbstractStringBuilder append(char c)

    public AbstractStringBuilder append(int i) {
        if (i == Integer.MIN_VALUE) {
            append("-2147483648");
            return this;
        }
        int appendedLength = (i < 0) ? Integer.stringSize(-i) + 1 // 负数需要多开辟1个'-'号的空间
                                     : Integer.stringSize(i);
        int spaceNeeded = count + appendedLength;
        ensureCapacityInternal(spaceNeeded);
        Integer.getChars(i, spaceNeeded, value);
        count = spaceNeeded;
        return this;
    }

    public AbstractStringBuilder append(long l) {
        if (l == Long.MIN_VALUE) {
            append("-9223372036854775808");
            return this;
        }
        int appendedLength = (l < 0) ? Long.stringSize(-l) + 1
                                     : Long.stringSize(l);
        int spaceNeeded = count + appendedLength;
        ensureCapacityInternal(spaceNeeded);
        Long.getChars(l, spaceNeeded, value);
        count = spaceNeeded;
        return this;
    }

    public AbstractStringBuilder append(float f) {
        FloatingDecimal.appendTo(f,this);
        return this;
    }

    public AbstractStringBuilder append(double d) {
        FloatingDecimal.appendTo(d,this);
        return this;
    }
```

> 附: `Integer`和`Long`的stringSize实现对比。
    
```java
    // Integer.stringSize
    final static int [] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
                                        99999999, 999999999, Integer.MAX_VALUE }; // 可以看出这又是一个以空间换时间的例子

    static int stringSize(int x) {
        for (int i=0; ; i++)
            if (x <= sizeTable[i])
                return i+1;
    }

    // Long.stringSize
    static int stringSize(long x) {
        long p = 10;
        for (int i=1; i<19; i++) { // 19: 与long取值范围(-9223372036854775808~9223372036854775807)有关
            if (x < p)
                return i;
            p = 10*p;　// long占用8字节, 空间成本比较大，且较之Integer的stringSize不太常用，这里采用以时间换空间的策略
        }
        return 19;
    }
```

#### Other

```java
    public int length() {
        return count;
    }

    public int capacity() {
        return value.length;
    }

    /** 确保容量至少等于规定的最小值。 */
    public void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity > 0)
            ensureCapacityInternal(minimumCapacity);
    }

    private void ensureCapacityInternal(int minimumCapacity) {
        // overflow-conscious code
        if (minimumCapacity - value.length > 0)
            expandCapacity(minimumCapacity); // 扩容
    }

    /**
     * 扩展容量：在不知道最终需要容量的情况下，指数扩展是一种常见策略，被广泛应用在各种内存分配相关的程序中。
     *
     * 指数扩容value.length * 2 + 2中的+2, 是为了防止原长度为0导致扩容失败。
     */
    void expandCapacity(int minimumCapacity) {
        int newCapacity = value.length * 2 + 2; // 默认扩容2倍，若指定扩容大于该值，则选择自定义的扩容值
        if (newCapacity - minimumCapacity < 0)
            newCapacity = minimumCapacity;
        if (newCapacity < 0) {
            if (minimumCapacity < 0) // overflow
                throw new OutOfMemoryError();
            newCapacity = Integer.MAX_VALUE;
        }
        value = Arrays.copyOf(value, newCapacity); // 旧值迁移
    }

    /** 修剪value数组到实际使用的长度,节约部分内存空间。 */
    public void trimToSize() {
        if (count < value.length) {
            value = Arrays.copyOf(value, count);
        }
    }

    /** 设置value数组长度。 */
    public void setLength(int newLength) {
        if (newLength < 0)
            throw new StringIndexOutOfBoundsException(newLength);
        ensureCapacityInternal(newLength);

        if (count < newLength) { // 大于当前实际长度，则扩容并以空字符填充新开辟空间。
            Arrays.fill(value, count, newLength, '\0');
        }

        count = newLength;
    }
```

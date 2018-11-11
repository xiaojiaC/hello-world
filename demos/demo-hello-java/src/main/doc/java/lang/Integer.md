## Integer


### 成员方法

#### 反转方法

```java
    /** 把int当作二进制，左边的位和右边的位进行互换。reverse是按位互换，reverseBytes是按字节(1字节=8bits)互换。*/
    public static int reverse(int i) {
        // Hacker's Delight, Figure 7-1
        i = (i & 0x55555555) << 1 | (i >>> 1) & 0x55555555;
        i = (i & 0x33333333) << 2 | (i >>> 2) & 0x33333333;
        i = (i & 0x0f0f0f0f) << 4 | (i >>> 4) & 0x0f0f0f0f;
        i = (i << 24) | ((i & 0xff00) << 8) |
            ((i >>> 8) & 0xff00) | (i >>> 24);
        return i;
    }

    public static int reverseBytes(int i) {
        return ((i >>> 24)           ) |
               ((i >>   8) &   0xFF00) |
               ((i <<   8) & 0xFF0000) |
               ((i << 24));
    }
```

eg: int i = 0x12345678。首先输出其二进制字符串，接着输出reverse后的二进制，最后输出reverseBytes的十六进制，输出为：

```
10010001101000101011001111000 // 原整数二进制表示
11110011010100010110001001000 // 按位反转后二进制表示

0x12345678 // 原整数十六进制表示
0x78563412 // 按字节反转后十六进制表示
```

reverseBytes是按字节翻转，`78`是十六进制表示的一个字节，`12`也是，所以结果`78563412`是比较容易理解的。

二进制翻转初看是不对的，这是因为输出不是32位，输出时忽略了前面的0，我们补齐32位再看就容易理解了：

```
00010010001101000101011001111000
00011110011010100010110001001000
```

高效实现位翻转的基本思路是：**首先交换相邻的单一位，然后以两位为一组，再交换相邻的位，接着是4位一组交换、然后是8位、16位，16位之后就完成了**。

这个思路不仅适用于二进制，而且适用于十进制。但对十进制而言，这个效率并不高，但对于二进制而言，却是高效的，因为**二进制可以在一条指令中交换多个相邻位**。

##### reverse算法解析: 

1. 开始1位交换：
```java
i & 0x55555555 = i & 01010101010101010101010101010101 // 取i二进制表示奇数位(第一位为0位)
i & 0xAAAAAAAA = i & 10101010101010101010101010101010 // 取i二进制表示偶数位
               = (i >>> 1) & 0x55555555               // 这里为节约内存，优化只使用0x55555555这一个常量
i = (i & 0x55555555) << 1 | (i >>> 1) & 0x55555555;   // i的奇数位向左移，偶数位向右移，然后通过|合并，达到相邻位互换的目的
```

2. 类似进行2, 4位交换：
```java
......
```

3. `注意`当进行到8, 16位交换时:
```java
i = ((i & 0x00ff00ff) << 8) | ((i >>> 8) & 0x00ff00ff);   // 按原思路继续进行8, 16位互换
i = ((i & 0x0000ffff) << 16) | ((i >>> 16) & 0x0000ffff);

i = ((i & 0xff00) << 8) | ((i >>> 8) & 0xff00);  // 这里摒弃原思路，而是只将32位中间的两个字节进行交换，之后直接将后8位直接移到最高位，前8位移动到最低位
i = (i << 24) | i | (i >>> 24);                  // 这里比原思路节约了一次位运算，这也就是reverseBytes的实现原理
```

> 是的，可以说你不得不敬畏每一行源码。

#### 循环移位

```java
    /* 循环左移（eg: 循环左移2位，则原来最高位两位会移动到最右边，就像左右相接的环一样，而不是普通移位直接丢弃高位，低位补0） */
    public static int rotateLeft(int i, int distance) {
        return (i << distance) | (i >>> -distance);
    }

    /* 循环右移 */
    public static int rotateRight(int i, int distance) {
        return (i >>> distance) | (i << -distance);
    }
```

eg: distance = 8; 则`i >>> -distance`，实际的移位个数不是后面的直接数字，而是直接数字的最低5位的值(`i & 0x1f`)，防止移位超过31位对int整数无效。

-8的二进制表示: `11111111111111111111111111111000`, `i >>> -8 = i >>> 24 // 11000`。


#### Other

```java
    /* 根据字符串返回其10进制表示 */
    public static Integer valueOf(String s) throws NumberFormatException {
        return Integer.valueOf(parseInt(s, 10));
    }

    public static Integer valueOf(String s, int radix) throws NumberFormatException {
        return Integer.valueOf(parseInt(s,radix));
    }

    /**
     * 享元模式：运行共享技术有效地支持大量细粒度对象的复用。
     * - 共享轻量级元素
     *
     * 默认情况下，预保留[-128, 127]共256个Integer对象，因此在构建Integer对象时，若在该缓存范围内应使用valueOf方法，而不是直接通过new创建。
     */
    public static Integer valueOf(int i) {
        if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);
    }
```

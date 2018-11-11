package xj.love.hj.demo.hello.java.java.lang;

import java.util.HashMap;
import java.util.Hashtable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * {@link java.util.Map}家族：
 * <pre>
 * - 安全性(多线程并发访问，无需关心线程的调度方式或执行时序，且在主调代码中不需要任何额外的同步或协同，这个类都能表现出正确的行为)
 * - 顺序性(元素迭代顺序跟最初放置到Map中的顺序保持一致)
 * </pre>
 *
 * {@link java.util.Hashtable}：线程安全。无序。键/值均不允许为null。支持Enumeration和Iterator遍历。通过哈希表实现。
 * 适用场景：多线程环境全表锁定
 *
 * {@link java.util.Properties}：线程安全。继承自Hashtable。 适用场景：应用配置文件
 *
 * {@link java.util.HashMap}：非线程安全。无序。键/值均允许为null, 键只能有一个null, 值可以有多个null，但不建议在map中放null值，
 * 因为在不存在的key上获取值时也返回null。支持Iterator遍历。通过哈希表(数组桶和链地址法)实现。 适用场景：单线程环境，无需同步效率高
 *
 * {@link java.util.TreeMap}：非线程安全。有序的散列表。通过红黑树实现。 适用场景：单线程中存储自然或自定义顺序的映射。
 *
 * {@link java.util.LinkedHashMap}：非线程安全。有序的散列表。继承自HashMap，通过维护Entity的引用链表实现。
 * 适用场景：单线程中存储插入或访问顺序的映射。例如：LRU缓存
 *
 * {@link java.util.IdentityHashMap}：非线程安全。无序。键的值可以重复，但引用不能相等，比较键时使用引用相等性(==)代替对象相等性(equals())。
 * 适用场景：需要引用相等性语义
 *
 * {@link java.util.EnumMap}：非线程安全。以枚举值的自然顺序排序。键必须是单个枚举类的枚举值，创建时必须显式或隐式指定它对应的枚举类。通过数组实现。
 * 适用场景：可枚举键值对映射。例如：错误码和错误消息定义
 *
 * {@link java.util.WeakHashMap}：未实现Cloneable和Serializable接口，不可克隆和序列化。键是“弱引用”类型，下一次GC不管当前内存是否足够均被回收。
 * 适用场景：重要性差异缓存
 *
 * {@link java.util.concurrent.ConcurrentHashMap}：线程安全。通过锁分段和热数据分解实现。 适用场景：多线程环境分段锁定
 *
 * {@link java.util.concurrent.ConcurrentSkipListMap}：线程安全。有序的散列表。通过跳表实现。 适用场景：多线程中存储自然或自定义顺序的映射。
 */
public class MapTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    @SuppressWarnings("JdkObsolete")
    public void testHashTable() {
        Hashtable<String, Object> map = new Hashtable<>();
        expectedException.expect(NullPointerException.class);
        map.put(null, 1); // key不能为null

        expectedException.expect(NullPointerException.class);
        map.put("a", null); // value不能为null
    }

    @Test
    public void testHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(null, null);
        map.put(null, 1);
        map.put("b", null);
        map.put("c", null);

        assertEquals(1, map.get(null)); // key可为null, 且只能有一个
        assertEquals(null, map.get("c")); // value可为null, 但可以有多个
        assertEquals(null, map.get("d")); // 不存在的key, 获取值时也返回null
    }
}

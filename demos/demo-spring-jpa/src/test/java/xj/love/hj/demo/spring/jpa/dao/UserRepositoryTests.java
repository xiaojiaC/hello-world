package xj.love.hj.demo.spring.jpa.dao;

import com.querydsl.core.types.Predicate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import org.hamcrest.core.StringContains;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import xj.love.hj.demo.spring.jpa.dao.specs.UserSpecs;
import xj.love.hj.demo.spring.jpa.domain.Address;
import xj.love.hj.demo.spring.jpa.domain.QUser;
import xj.love.hj.demo.spring.jpa.domain.User;
import xj.love.hj.demo.spring.jpa.dto.UserDto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

/**
 * 用户实体DAO测试
 *
 * @author xiaojia
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Rule
    public final OutputCapture outputCapture = new OutputCapture();

    @Autowired
    private UserRepository userRepository;

    /**
     * 3.3.2. 存储库方法的null处理
     */
    @Test(expected = EmptyResultDataAccessException.class)
    public void findByFirstName() {
        userRepository.findByFirstName("invalidName");
    }

    /**
     * 3.3.2. 存储库方法的null处理
     */
    @Test
    public void findByLastName() {
        Optional<User> userOptional = userRepository.findByLastName("cui");

        assertEquals(true, userOptional.isPresent());
    }

    /**
     * 3.3.2.1. 可空性注解
     */
    @Test
    public void findByAddress() {
        Address address = new Address();
        address.setId(1L);

        User user = userRepository.findByAddress(address);

        assertNotNull(user);
        assertEquals("bob", user.getFirstName());
        assertEquals("cui", user.getLastName());
    }

    /**
     * 3.4.3. 属性表达式
     */
    @Test
    public void findByAddressZipCode() {
        List<User> users = userRepository.findByAddress_ZipCode("200120");

        assertEquals(1, users.size());
        assertEquals("bob", users.get(0).getFirstName());
        assertEquals("cui", users.get(0).getLastName());
    }

    /**
     * <pre>
     * 3.4.4. 特殊参数处理
     * 3.4.5. 限制查询结果
     * </pre>
     */
    @Test
    public void findByAge() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Order.asc("firstName")));
        Slice<User> userSlice = userRepository.findTop3ByAge(18, pageRequest);

        assertEquals(true, userSlice.hasContent());
        assertEquals(1, userSlice.getNumberOfElements());
        assertEquals("sunny", userSlice.getContent().get(0).getFirstName());
    }

    /**
     * 3.4.6. 流式查询结果
     */
    @Test
    @Transactional(readOnly = true)
    public void findByGender() {
        try (Stream<User> userStream = userRepository.findByGender(0)) {
            User user = userStream.filter(u -> u.getAddress() != null).findFirst()
                    .orElse(null);
            assertNotNull(user);
        }
    }

    /**
     * 3.4.7. 异步查询结果
     */
    @Test
    public void findByAddressId() {
        CompletableFuture<User> userCompletableFuture = userRepository.findByAddressId(1L);

        userCompletableFuture.thenAccept(u -> assertEquals("bob",
                u.getFirstName()));
    }

    /**
     * 3.6.1. 自定义单个存储库
     */
    @Test
    public void findByFullName() {
        User user = userRepository.findByFullName("bob.cui");

        assertNotNull(user);
        assertEquals("bob", user.getFirstName());
        assertEquals("cui", user.getLastName());
    }

    /**
     * 3.7. 从聚合根发布事件
     */
    @Test
    @Transactional
    // @Rollback
    public void save() {
        User user = new User();
        user.setId(3L);
        user.setGender(0);
        user.setAge(28);
        user.setFirstName("tony");
        user.setLastName("wang");

        userRepository.save(user);

        outputCapture.expect(StringContains.containsString("Domain event process"));
        outputCapture.expect(StringContains.containsString("Domain event publish after callback"));
    }

    /**
     * 3.8.1. Querydsl扩展
     */
    @Test
    public void findByQuerydsl() {
        Predicate predicate = QUser.user.firstName.equalsIgnoreCase("Sunny")
                .and(QUser.user.lastName.eq("li"));

        Optional<User> userOptional = userRepository.findOne(predicate);

        assertEquals(true, userOptional.isPresent());
        assertEquals("sunny", userOptional.get().getFirstName());
        assertNull(userOptional.get().getAddress());
    }

    /**
     * 4.3.3. 使用JPA命名查询
     */
    @Test
    public void findByAddressCity() {
        List<User> users = userRepository.findByAddressCity("上海");

        assertNotNull(users);
        assertEquals(1, users.size());
    }

    /**
     * <pre>
     * 使用高级LIKE表达式
     * 本地查询
     * </pre>
     */
    @Test
    public void findByFirstNameStartsWith() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<User> page = userRepository.findByFirstNameStartsWith("b", pageRequest);

        assertEquals(1, page.getNumberOfElements());
        assertEquals("bob", page.getContent().get(0).getFirstName());
    }

    /**
     * 4.3.5. 使用排序
     */
    @Test
    public void findByLastNameAndSort() {
        List<User> users = userRepository.findByLastNameAndSort("i",
                JpaSort.unsafe("LENGTH(firstName)"));

        assertEquals(2, users.size());
        assertEquals("bob", users.get(0).getFirstName());
        assertEquals("sunny", users.get(1).getFirstName());
    }

    /**
     * 4.3.6. 使用命名参数
     */
    @Test
    public void findByLastNameAndFirstName() {
        User user = userRepository.findByLastNameAndFirstName("cui", "bob");

        assertNotNull(user);
    }

    /**
     * <pre>
     * 4.3.7. 使用SpEL表达式
     * 4.7.1. 事务性查询方法
     * </pre>
     */
    @Test
    @Transactional
    public void updateFirstNameById() {
        int updatedNum = userRepository.updateFirstNameById(1L, "xiaojia");

        assertEquals(1, updatedNum);
        Optional<User> updatedUser = userRepository.findById(1L);
        updatedUser.ifPresent(u -> assertEquals("xiaojia", u.getFirstName()));
    }

    /**
     * 4.3.10. 配置Fetch-和LoadGraphs
     */
    @Test
    public void findByFirstNameAndLastName() {
        User user = userRepository.findByFirstNameAndLastName("bob", "cui");

        assertEquals("bob", user.getFirstName());
        assertNotNull(user.getAddress());
        assertEquals("浦东新区", user.getAddress().getDistrict());
    }

    /**
     * 4.3.11. 投影
     */
    @Test
    public void findByAgeBetween() {
        List<UserDto> userDtos = userRepository.findByAgeBetween(16, 18);

        assertEquals(1, userDtos.size());
        assertEquals(Long.valueOf(2), userDtos.get(0).getId());
        assertEquals("sunny", userDtos.get(0).getFirstName());
        assertEquals("li", userDtos.get(0).getLastName());
    }

    /**
     * 4.6. 按示例查询
     */
    @Test
    @Transactional
    public void findByExample() {
        User user = new User();
        user.setLastName("C");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("lastName", startsWith().ignoreCase());

        Optional<User> userOptional = userRepository.findOne(Example.of(user, matcher));

        assertEquals(true, userOptional.isPresent());
        assertEquals("bob", userOptional.get().getFirstName());
    }

    /**
     * 4.5. 规范
     */
    @Test
    public void findAll() {
        List<User> users = userRepository.findAll(UserSpecs.adult());

        assertEquals(2, users.size());

        // 查找上海地区方圆60km内，乐观/开朗的用户
        Set<String> tags = new HashSet<>();
        tags.add("乐观");
        tags.add("开朗");
        users = userRepository.findAll(UserSpecs.custom(tags, "121", "31", 60000));

        assertEquals(1, users.size());
    }

    /**
     * 4.9. 审计
     */
    @Test
    @Ignore("See demo-spring-session")
    public void audit() {
    }
}

package xj.love.hj.demo.spring.jpa.dao;

import com.querydsl.core.types.dsl.StringPath;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import xj.love.hj.demo.spring.jpa.dao.base.BaseRepository;
import xj.love.hj.demo.spring.jpa.dao.fragment.CustomizedUserRepository;
import xj.love.hj.demo.spring.jpa.domain.Address;
import xj.love.hj.demo.spring.jpa.domain.QUser;
import xj.love.hj.demo.spring.jpa.domain.User;
import xj.love.hj.demo.spring.jpa.dto.UserDto;

/**
 * 用户实体DAO
 *
 * @author xiaojia
 * @since 1.0
 */
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User, Long>,
        CustomizedUserRepository,
        JpaSpecificationExecutor,
        QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {

    User findByFirstName(String firstName);

    Optional<User> findByLastName(String lastName);

    @Nullable
    User findByAddress(Address address);

    List<User> findByAddress_ZipCode(String zipCode);

    Slice<User> findTop3ByAge(int age, Pageable pageable);

    @Transactional(readOnly = true)
    Stream<User> findByGender(int gender);

    @Async
    CompletableFuture<User> findByAddressId(Long addressId);

    // https://stackoverflow.com/questions/36600550/passing-array-of-reg-expressions-to-spring-based-mongo-query/36647967#36647967
    @Override
    default void customize(QuerydslBindings bindings, QUser user) {
        // 若为lastName属性，则做相等匹配
        bindings.bind(user.lastName).first((path, value) -> path.eq(value));
        // 若为字符串属性-字符串参数，则做相等忽略大小写匹配
        bindings.bind(String.class)
                .first((StringPath path, String value) -> path.equalsIgnoreCase(value));
        // 若为字符串属性-集合参数，则做in匹配
        bindings.bind(String.class)
                .all((StringPath path, Collection<? extends String> values) -> Optional
                        .of(path.in(values)));
        // 若为addressId属性，则剔除
        bindings.excluding(user.addressId);
    }

    List<User> findByAddressCity(String city);

    @Query(value = "select * from dsj_user u where u.first_name like ?1%",
            countQuery = "select count(*) from dsj_user u where u.first_name like ?1%",
            nativeQuery = true)
    Page<User> findByFirstNameStartsWith(String distinct, Pageable pageable);

    @Query("select u from User u where u.lastName like %?1%")
    List<User> findByLastNameAndSort(String lastName, Sort sort);

    @Query("select u from User u where u.firstName = :fistName and u.lastName = :lastName")
    User findByLastNameAndFirstName(@Param("lastName") String lastName,
            @Param("fistName") String fistName);

    @Modifying
    @Transactional
    @Query("update #{#entityName} u set u.firstName = ?2 where u.id = ?1")
    int updateFirstNameById(Long id, String firstName);

    @EntityGraph(value = "User.detail", type = EntityGraphType.LOAD)
        // @EntityGraph(attributePaths = { "address" })
    User findByFirstNameAndLastName(String firstName, String lastName);

    List<UserDto> findByAgeBetween(int ageMin, int ageMax);
}

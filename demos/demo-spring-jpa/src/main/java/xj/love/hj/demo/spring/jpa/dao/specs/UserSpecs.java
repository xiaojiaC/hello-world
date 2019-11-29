package xj.love.hj.demo.spring.jpa.dao.specs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import xj.love.hj.demo.spring.jpa.domain.Address;
import xj.love.hj.demo.spring.jpa.domain.User;

/**
 * 用户查询规格
 *
 * @author xiaojia
 * @see <a href="https://dev.mysql.com/doc/refman/5.7/en/functions.html">MySQL functions</a>
 * @since 1.0
 */
public class UserSpecs {

    private static final int ADULT_MIN_AGE = 18;

    /**
     * 成年用户规格
     */
    public static Specification<User> adult() {
        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query,
                    CriteriaBuilder builder) {
                return builder.greaterThanOrEqualTo(root.get("age"), ADULT_MIN_AGE);
            }
        };
    }

    /**
     * 自定义规格
     *
     * @param tags 含有的标签
     * @param longitude 经度
     * @param latitude 纬度
     * @param distance 中心点距离
     */
    public static Specification<User> custom(Set<String> tags, String longitude, String latitude,
            Integer distance) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            buildInTagsPredicate(tags, root, query, builder).ifPresent(predicates::add);

            Square square = new Square(longitude, latitude, distance);
            buildSquareDistancePredicate(square, root, query, builder).ifPresent(predicates::add);

            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * <pre>
     *     mysql> SELECT FIND_IN_SET('b','a,b,c,d');
     *         -> 2
     * </pre>
     */
    private static Optional<Predicate> buildInTagsPredicate(Set<String> tags, Root<User> root,
            CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (CollectionUtils.isEmpty(tags)) {
            return Optional.empty();
        }
        List<Predicate> predicates = tags.stream()
                .filter(StringUtils::isNotBlank)
                .map(tag -> {
                    LiteralExpression expression = new LiteralExpression(null, tag);
                    Expression<Integer> result = builder.function("find_in_set", Integer.class,
                            expression, root.get("tags"));
                    return builder.greaterThanOrEqualTo(result, 1);
                }).collect(Collectors.toList());
        return Optional.of(builder.or(predicates.toArray(new Predicate[predicates.size()])));
    }

    /**
     * <pre>
     *     mysql> SET @pt1 = ST_GeomFromText('POINT(0 0)');
     *     mysql> SET @pt2 = ST_GeomFromText('POINT(180 0)');
     *     mysql> SELECT ST_Distance_Sphere(@pt1, @pt2);
     *         -> 20015042.813723423
     * </pre>
     */
    private static Optional<Predicate> buildSquareDistancePredicate(Square square,
            Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (square == null) {
            return Optional.empty();
        }

        Join<User, Address> join = root.join("address", JoinType.LEFT);

        Expression<Integer> point1 = builder.function("point", Integer.class, join.get("longitude"),
                join.get("latitude"));

        LiteralExpression longitude = new LiteralExpression(null, square.getLongitude());
        LiteralExpression latitude = new LiteralExpression(null, square.getLatitude());
        Expression<Integer> point2 = builder.function("point", Integer.class, longitude, latitude);

        Expression<Integer> result = builder.function("st_distance_sphere", Integer.class,
                point1, point2);
        return Optional.of(builder.lessThanOrEqualTo(result, square.getDistance()));
    }

    /**
     * 方圆距离
     */
    @Data
    @AllArgsConstructor
    public static class Square {

        private String longitude;
        private String latitude;
        private Integer distance;
    }
}

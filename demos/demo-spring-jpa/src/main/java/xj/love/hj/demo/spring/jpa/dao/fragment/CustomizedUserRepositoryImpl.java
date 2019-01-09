package xj.love.hj.demo.spring.jpa.dao.fragment;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import xj.love.hj.demo.spring.jpa.domain.User;

/**
 * 自定义用户DAO片段实现
 *
 * @author xiaojia
 * @since 1.0
 */
public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findByFullName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return null;
        }
        String[] names = fullName.split("\\.");
        if (names.length != 2) {
            return null;
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("firstName"), names[0]));
        predicates.add(cb.equal(root.get("lastName"), names[1]));

        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query).getSingleResult();
    }
}

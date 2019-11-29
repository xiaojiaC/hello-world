package xj.love.hj.demo.spring.session.common.dao.base;

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * 基础仓库实现
 *
 * @param <T> 实体类型
 * @param <IdT> 实体id类型
 * @author xiaojia
 * @since 1.0
 */
public class BaseRepositoryImpl<T, IdT extends Serializable> extends
        SimpleJpaRepository<T, IdT> implements BaseRepository<T, IdT> {

    private final Class<T> domainClass;
    private final EntityManager entityManager;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.domainClass = domainClass;
        this.entityManager = entityManager;
    }
}

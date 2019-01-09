package xj.love.hj.demo.spring.jpa.dao.base;

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;

/**
 * 基础仓储实现
 *
 * @param <T> 实体类型
 * @param <IdT> 实体id类型
 * @author xiaojia
 * @since 1.0
 */
public class BaseRepositoryImpl<T, IdT extends Serializable> extends QuerydslJpaRepository<T, IdT>
        implements BaseRepository<T, IdT> {

    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<T, IdT> entityInformation,
            EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    // 你的自定义实现
}

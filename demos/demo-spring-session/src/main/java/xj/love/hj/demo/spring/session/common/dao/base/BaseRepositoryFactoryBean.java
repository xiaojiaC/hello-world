package xj.love.hj.demo.spring.session.common.dao.base;

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * 基础仓库工厂
 *
 * @param <R> JPA存储库类型
 * @param <T> 实体类型
 * @param <IdT> 实体id类型
 * @author xiaojia
 * @since 1.0
 */
public class BaseRepositoryFactoryBean<R extends JpaRepository<T, IdT>, T, IdT extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, IdT> {

    public BaseRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new SimpleRepositoryFactory(em);
    }

    /**
     * 简单的JPA仓库工厂
     *
     * @param <T> 实体类型
     * @param <IdT> 实体id类型
     */
    private static class SimpleRepositoryFactory<T, IdT extends Serializable> extends
            JpaRepositoryFactory {

        private final EntityManager em;

        SimpleRepositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
        }

        @Override
        protected Object getTargetRepository(RepositoryInformation information) {
            return new BaseRepositoryImpl<T, IdT>((Class<T>) information.getDomainType(), em);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return BaseRepositoryImpl.class;
        }
    }
}

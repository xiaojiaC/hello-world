package xj.love.hj.demo.spring.jpa.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 基础仓储
 *
 * @author xiaojia
 * @since 1.0
 * @param <T> 实体类型
 * @param <IdT> 实体id类型
 */
@NoRepositoryBean
public interface BaseRepository<T, IdT> extends JpaRepository<T, IdT> {

}

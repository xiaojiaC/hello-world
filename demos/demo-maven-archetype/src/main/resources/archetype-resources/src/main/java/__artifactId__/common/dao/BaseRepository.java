package ${package}.${artifactId}.common.dao;

import ${package}.${artifactId}.common.po.IdGeneratorPo;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 基础实体仓库。
 *
 * @author xiaojia
 * @since 1.0
 */
@NoRepositoryBean
public interface BaseRepository<T extends IdGeneratorPo, ID extends Serializable> extends
        PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor {

}

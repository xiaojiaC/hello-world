package xj.love.hj.demo.solr.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xj.love.hj.demo.solr.domain.Product;

/**
 * 产品仓储自定义
 *
 * @author xiaojia
 * @since 1.0
 */
public interface ProductRepositoryCustom {

    Page<Product> findProductsByCustomImplementation(String value, Pageable page);

    void setInventory(long id, int inventory);
}

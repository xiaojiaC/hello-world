package xj.love.hj.demo.solr.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xj.love.hj.demo.solr.domain.Product;

/**
 * 产品服务
 *
 * @author xiaojia
 * @since 1.0
 */
public interface ProductService {

    /**
     * 根据关键字查询产品
     *
     * @param keyword 关键字
     * @param page 分页
     * @return 产品列表
     */
    Page<Product> findByKeyword(String keyword, Pageable page);

    /**
     * 根据标题查询产品
     *
     * @param title 标题
     * @param page 分页
     * @return 产品列表
     */
    Page<Product> findByTitle(String title, Pageable page);

    /**
     * 查找所有的裤子
     *
     * @param page 分页
     * @return 产品列表
     */
    Page<Product> findAllTrousers(Pageable page);

    /**
     * 设置产品库存
     *
     * @param id 产品ID
     * @param inventory 库存数
     */
    void setInventory(long id, int inventory);
}

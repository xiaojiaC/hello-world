package xj.love.hj.demo.solr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xj.love.hj.demo.solr.dao.ProductRepository;
import xj.love.hj.demo.solr.domain.Product;
import xj.love.hj.demo.solr.service.ProductService;

/**
 * 产品服务实现
 *
 * @author xiaojia
 * @since 1.0
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> findByKeyword(String keyword, Pageable page) {
        return productRepository.findByContentMatchesOrderByPriceDesc(keyword, page);
    }

    @Override
    public Page<Product> findByTitle(String title, Pageable page) {
        return productRepository.findProductsByCustomImplementation(title, page);
    }

    @Override
    public Page<Product> findAllTrousers(Pageable page) {
        return productRepository.findByTitleAndFacetOnBrand("裤", page);
    }

    @Override
    public void setInventory(long id, int inventory) {
        productRepository.setInventory(id, inventory);
    }
}

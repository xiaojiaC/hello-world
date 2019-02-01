package xj.love.hj.demo.solr.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xj.love.hj.demo.solr.domain.Product;
import xj.love.hj.demo.solr.service.ProductService;

/**
 * 产品控制器（仅用作测试）
 *
 * @author xiaojia
 * @since 1.0
 */
@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Page<Product> findProduct(Pageable page,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String keyword) {
        if (StringUtils.isNotBlank(title)) {
            return productService.findByTitle(title, page);
        }

        if (StringUtils.isNotBlank(keyword)) {
            return productService.findByKeyword(keyword, page);
        }

        return productService.findAllTrousers(page);
    }

    // 体验原子更新特性
    @PutMapping(value = "{id}")
    public void updateProductInventory(@PathVariable long id, int inventory) {
        productService.setInventory(id, inventory);
    }
}

package xj.love.hj.demo.solr.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import xj.love.hj.demo.solr.dao.ProductRepositoryCustom;
import xj.love.hj.demo.solr.domain.Product;

/**
 * 产品仓储自定义实现
 *
 * @author xiaojia
 * @since 1.0
 */
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private SolrOperations solrTemplate;

    @Autowired
    public void setOperations(SolrOperations solrTemplate) {
        this.solrTemplate = solrTemplate;
    }

    @Override
    public Page<Product> findProductsByCustomImplementation(String value, Pageable page) {
        Criteria criteria = new SimpleStringCriteria("title:" + value);
        Query query = new SimpleQuery(criteria).setPageRequest(page);
        return solrTemplate.queryForPage("product", query, Product.class);
    }

    @Override
    public void setInventory(long id, int inventory) {
        PartialUpdate update = new PartialUpdate("id", id + "");
        update.add("inventory", inventory);
        solrTemplate.saveBean("product", update);
        solrTemplate.commit("product");
    }
}

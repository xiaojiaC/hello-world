package xj.love.hj.demo.solr.dao;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Boost;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;
import xj.love.hj.demo.solr.domain.Product;

/**
 * 产品数据访问对象
 *
 * @author xiaojia
 * @since 1.0
 */
@Repository
public interface ProductRepository extends SolrCrudRepository<Product, Long>,
        ProductRepositoryCustom {

    // 引用命名查询 Product.findByBrand
    // q=brand:<brand>
    @Query(name = "Product.findByBrand", fields = {"id", "title", "description"})
    List<Product> findByAnnotatedNamedQuery(String brand);

    // q=content:<content>&start=<page.number>&rows=<page.size>
    Page<Product> findByContentMatchesOrderByPriceDesc(String content, Pageable page);

    // 将在确定元素总数之前执行计数查询
    // q=title:<title>*&start=0&rows=<result of count query for q=name:<name>>
    List<Product> findByTitleStartingWith(String name);

    // q=title:<title>&facet=true&facet.field=brand&facet.limit=20&start=<page.number>&rows=<page.size>
    @Query(value = "title:?0")
    @Facet(fields = {"brand"}, limit = 20)
    FacetPage<Product> findByTitleAndFacetOnBrand(String title, Pageable page);

    // q=title:<title>^2 OR description:<description>&start=<page.number>&rows=<page.size>
    Page<Product> findByTitleOrDescription(@Boost(2) String title, String description,
            Pageable page);

    // q=brand:(<brand...>)&hl=true&hl.fl=*
    @Highlight
    HighlightPage<Product> findByBrandIn(Collection<String> brand, Pageable page);

    // q=location:[<box.start.latitude>,<box.start.longitude> TO <box.end.latitude>,<box.end.longitude>]
    // Page<Product> findByLocationNear(Box box);

    // q={!geofilt pt=<location.latitude>,<location.longitude> sfield=location d=<distance.value>}
    // Page<Product> findByLocationWithin(Point location, Distance distance);
}

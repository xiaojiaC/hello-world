package xj.love.hj.demo.solr.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

/**
 * 产品实体域
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@AllArgsConstructor
@SolrDocument(collection = "product")
public class Product {

    @Id
    @Field
    private Long id;

    @Field
    private String title;

    @Field
    private String description;

    @Field
    private String color;

    @Field
    private String size;

    @Field
    private BigDecimal price;

    @Field
    private Integer inventory;

    @Field
    private String brand;

    @Field("to_market_at")
    private Date toMarketAt;

    @Field
    private Integer status;

    @Field("created_at")
    private Date createdAt;

    @Field("updated_at")
    private Date updatedAt;

    @Field
    private List<String> images;

    @Field
    private String content;
}

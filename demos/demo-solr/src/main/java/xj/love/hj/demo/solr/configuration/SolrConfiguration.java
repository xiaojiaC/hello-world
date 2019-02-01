package xj.love.hj.demo.solr.configuration;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

/**
 * Solr相关配置
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
@EnableSolrRepositories(
        basePackages = "xj.love.hj.demo.solr.dao"
)
public class SolrConfiguration {

    @Value("${solr.base-url}")
    private String baseUrl;

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient.Builder().withBaseSolrUrl(baseUrl).build();
    }

    @Bean
    public SolrOperations solrTemplate() {
        return new SolrTemplate(solrClient());
    }
}

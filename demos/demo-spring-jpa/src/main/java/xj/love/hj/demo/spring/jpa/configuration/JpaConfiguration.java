package xj.love.hj.demo.spring.jpa.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xj.love.hj.demo.spring.jpa.dao.base.BaseRepositoryImpl;


/**
 * JPA配置
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "xj.love.hj.demo.spring.jpa.dao",
        repositoryBaseClass = BaseRepositoryImpl.class
)
@EnableTransactionManagement
public class JpaConfiguration {

    /**
     * 配置仓库填充者
     */
    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();

        // Set a custom ObjectMapper if Jackson customization is needed
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        factory.setMapper(objectMapper);

        Resource sourceData = new ClassPathResource("data.json");
        factory.setResources(new Resource[]{sourceData});

        return factory;
    }
}

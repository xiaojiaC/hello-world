package xj.love.hj.demo.spring.session.common.configuration;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xj.love.hj.demo.spring.session.common.constant.Constants;
import xj.love.hj.demo.spring.session.common.context.Visitor;
import xj.love.hj.demo.spring.session.common.context.WebThreadContext;
import xj.love.hj.demo.spring.session.common.dao.base.BaseRepositoryFactoryBean;

/**
 * Spring JPA配置
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = Constants.BASE_PACKAGE,
        repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class
)
public class JpaConfiguration {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> Optional.ofNullable(
                Optional.ofNullable(WebThreadContext.getVisitor())
                        .map(Visitor::getId)
                        .orElse(null)
        );
    }
}

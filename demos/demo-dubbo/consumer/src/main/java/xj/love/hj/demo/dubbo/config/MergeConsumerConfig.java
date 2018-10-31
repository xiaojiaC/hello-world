package xj.love.hj.demo.dubbo.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xj.love.hj.demo.dubbo.service.MergeService;

/**
 * 分组合并菜单服务消费者配置。
 *
 * @author xiaojia
 * @since 1.0
 */
@Configuration
public class MergeConsumerConfig {

    /**
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/group-merger.html">分组聚合</a>
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/result-cache.html">结果缓存</a>
     */
    @Bean
    public List<MethodConfig> mergeMethodsConfig() {
        List<MethodConfig> methods = new ArrayList<>();

        //        MethodConfig methodConfig = new MethodConfig();
        //        methodConfig.setName("mainMenus"); // 指定方法合并结果，其它未指定的方法，将只调用一个 Group
        //        methodConfig.setMerger("true");

        //        MethodConfig methodConfig = new MethodConfig();
        //        methodConfig.setName("mainMenus"); // 某个方法不合并结果，其它都合并结果, 需搭配41行配置使用
        //        methodConfig.setMerger("false");

        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("mainMenus"); // 指定合并策略，缺省根据返回值类型自动匹配，如果同一类型有两个合并器时，需指定合并器的名称
        methodConfig.setMerger("myMerger");
        methodConfig.setCache("lru"); // 结果缓存，用于加速热门数据的访问速度。lru 基于最近最少使用原则删除多余缓存，保持最热的数据被缓存。

        methods.add(methodConfig);
        return methods;
    }

    /**
     * 菜单服务配置
     */
    @Bean
    public ReferenceConfig mergeReferenceConfig(ApplicationConfig applicationConfig,
            RegistryConfig registryConfig) {
        ReferenceConfig<MergeService> reference = new ReferenceConfig<>();
        reference.setId("mergeService");
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);
        reference.setInterface(MergeService.class);
        reference.setVersion("1.0.0");
        reference.setGroup("*"); // 或 reference.setGroup("merge,merge2");
        // reference.setMerger("true");
        reference.setMethods(mergeMethodsConfig());
        reference.setLoadbalance("roundrobin");
        return reference;
    }

}

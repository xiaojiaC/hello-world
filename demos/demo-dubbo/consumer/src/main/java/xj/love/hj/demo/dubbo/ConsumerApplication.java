package xj.love.hj.demo.dubbo;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.service.EchoService;
import com.alibaba.dubbo.rpc.service.GenericService;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import xj.love.hj.demo.dubbo.model.User;
import xj.love.hj.demo.dubbo.service.DemoService;
import xj.love.hj.demo.dubbo.service.MergeService;
import xj.love.hj.demo.dubbo.service.ValidationService;

/**
 * 消费者应用。
 *
 * @author xiaojia
 * @since 1.0
 */
@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication
                .run(ConsumerApplication.class, args);

        testDemoService(context);
        testMergeService(context);
        testValidationService(context);
        testGenericBarService(context);
        testEchoService(context);
    }

    private static void testDemoService(ApplicationContext context) {
        ReferenceConfig reference = (ReferenceConfig) context.getBean("referenceConfig");
        DemoService demoService = (DemoService) reference.get();
        // execute remote invocation
        String hello = demoService.sayHello("world");
        // show the result
        System.out.println("---------------------->" + hello);
    }

    private static void testMergeService(ApplicationContext context) {
        ReferenceConfig reference = (ReferenceConfig) context.getBean("mergeReferenceConfig");
        MergeService mergeService = (MergeService) reference.get();
        List<String> mainMenus = mergeService.mainMenus();
        System.out.println("---------------------->" + mainMenus);

        List<String> subMenus = mergeService.subMenus();
        System.out.println("---------------------->" + subMenus);
    }

    private static void testValidationService(ApplicationContext context) {
        ReferenceConfig reference = (ReferenceConfig) context.getBean("validationReferenceConfig");
        ValidationService validationService = (ValidationService) reference.get();
        try {
            User user = new User();
            user.setName("zhangsan");
            user.setAge(1);
            user.setEmail("zhangsan@qq.com");
            validationService.save(user);
            System.out.println("Validation passed");
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e
                    .getConstraintViolations(); // 可以拿到一个验证错误详细信息的集合
            System.out.println("---------------------->" + violations);
        }
    }

    private static void testGenericBarService(ApplicationContext context) {
        ReferenceConfig reference = (ReferenceConfig) context.getBean("genericReferenceConfig");
        GenericService genericDemoService = (GenericService) reference.get();
        Object result = genericDemoService
                .$invoke("sayHello", new String[]{"java.lang.String"}, new Object[]{"World"});
        System.out.println("---------------------->" + result);
    }

    /**
     * 回声测试用于检测服务是否可用，回声测试按照正常请求流程执行，能够测试整个调用是否通畅，可用于监控。
     * 所有服务自动实现 EchoService 接口，只需将任意服务引用强制转型为 EchoService，即可使用。
     *
     * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/echo-service.html">回声测试</a>
     */
    private static void testEchoService(ApplicationContext context) {
        ReferenceConfig reference = (ReferenceConfig) context.getBean("referenceConfig");
        DemoService demoService = (DemoService) reference.get();
        EchoService echoService = (EchoService) demoService; // 强制转型为EchoService
        String status = echoService.$echo("OK").toString();
        Assert.isTrue(status.equals("OK"), "----------------------> status error");
    }

}

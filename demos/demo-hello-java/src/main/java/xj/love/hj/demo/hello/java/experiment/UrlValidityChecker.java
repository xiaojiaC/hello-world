package xj.love.hj.demo.hello.java.experiment;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * URL有效性检查者。
 *
 * @author xiaojia
 * @since 1.0
 */
public class UrlValidityChecker {

    /**
     * <pre>
     * {@link Retention}的作用是定义被它所标注的注解保留多久。
     * SOURCE: 被编译器忽略。eg: {@see lombok.Data}
     * CLASS:  注解将会被保留在Class文件中，但在运行时并不会被VM保留。默认行为
     * RUNTIME:保留至运行时。可以通过反射去获取注解信息。eg: {@link IsUrl}
     * </pre>
     * <pre>
     * {@link Target}指明Annotation所修饰的对象范围。
     * CONSTRUCTOR:用于描述构造器
     * FIELD:用于描述域
     * LOCAL_VARIABLE:用于描述局部变量
     * METHOD:用于描述方法
     * PACKAGE:用于描述包
     * PARAMETER:用于描述参数
     * TYPE:用于描述类、接口(包括注解类型)或enum声明
     * </pre>
     * {@link Documented}该注解应该被作为被标注的成员的公共API，因此可以被例如javadoc此类的工具文档化。
     */

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    private @interface IsUrl {

    }

    /**
     * 测试实体
     */
    public static class Person {

        /**
         * 图像URL
         */
        public static final String AVATAR_URL = "avatarUrl";

        @IsUrl
        private String avatarUrl;

        public Person(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

    }

    public static Map<String, String> check(Person person) throws Exception {
        Map<String, String> violationConstraints = new HashMap<>();

        Class clazz = person.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation annotation = field.getAnnotation(IsUrl.class);
            if (annotation != null) {
                String urlField = (String) field.get(person);
                if (!isUrl(urlField)) {
                    violationConstraints.put(field.getName(), urlField);
                }
            }
        }

        return violationConstraints;
    }

    private static boolean isUrl(String str) {
        URL url;
        try {
            url = new URL(str);
            url.openConnection();
        } catch (Exception e) {
            url = null;
        }
        return url != null;
    }
}

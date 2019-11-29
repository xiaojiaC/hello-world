package xj.love.hj.demo.ast.papa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 织入标准getter方法。
 * <p>
 * 样例:
 * <pre>
 *     &#64;Getter
 *     public class Sample {
 *         private int foo;
 *     }
 * </pre>
 *
 * 将会生成:
 *
 * <pre>
 *     public class Sample {
 *         private int foo;
 *         public int getFoo() {
 *             return this.foo;
 *         }
 *     }
 * </pre>
 * </p>
 *
 * @author xiaojia
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Getter {

}

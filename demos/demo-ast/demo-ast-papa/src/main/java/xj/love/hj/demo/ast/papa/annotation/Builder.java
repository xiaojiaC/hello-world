package xj.love.hj.demo.ast.papa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 织入构造器。
 * <p>
 * 样例:
 * <pre>
 *     &#64;Builder
 *     public class Sample {
 *         private int foo;
 *         private String bar;
 *     }
 * </pre>
 *
 * 将会生成:
 *
 * <pre>
 *     public class SampleBuilder {
 *         private int foo;
 *
 *         public SampleBuilder foo(int foo) {
 *             this.foo = foo;
 *             return this;
 *         }
 *         public SampleBuilder bar(String bar) {
 *             this.bar = bar;
 *             return this;
 *         }
 *
 *         public Sample build() {
 *             Sample instance = new Sample();
 *             instance.setFoo(this.foo);
 *             return instance;
 *         }
 *     }
 * </pre>
 * </p>
 *
 * @author xiaojia
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Builder {

}

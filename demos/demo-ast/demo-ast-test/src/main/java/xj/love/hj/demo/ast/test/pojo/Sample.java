package xj.love.hj.demo.ast.test.pojo;

import xj.love.hj.demo.ast.papa.annotation.Builder;
import xj.love.hj.demo.ast.papa.annotation.Getter;

/**
 * 样例POJO
 *
 * @author xiaojia
 * @since 1.0
 */
@Getter
@Builder
public class Sample {

    private String name;
    private int foo;

    public void setName(String name) {
        this.name = name;
    }

    public void setFoo(int foo) {
        this.foo = foo;
    }

    @Override
    public String toString() {
        return String.format("Sample{name=%s, foo=%s}", getName(), getFoo());
    }
}

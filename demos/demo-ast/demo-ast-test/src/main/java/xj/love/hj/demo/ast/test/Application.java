package xj.love.hj.demo.ast.test;

import xj.love.hj.demo.ast.test.pojo.Sample;
import xj.love.hj.demo.ast.test.pojo.SampleBuilder;

/**
 * 主程序
 */
public class Application {

    public static void main(String[] args) {
        testGetter();
        testBuilder();
    }

    private static void testGetter() {
        Sample sample = new Sample();
        sample.setName("testGetter");
        sample.setFoo(1);
        System.out.println(sample);
    }

    private static void testBuilder() {
        Sample sample = new SampleBuilder()
                .name("testBuilder")
                .foo(2)
                .build();
        System.out.println(sample);
    }
}

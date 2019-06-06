package xj.love.hj.demo.ast.test.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xj.love.hj.demo.ast.test.pojo.Sample;
import xj.love.hj.demo.ast.test.pojo.SampleBuilder;

/**
 * 样例控制器
 *
 * @author xiaojia
 * @since 1.0
 */
@RestController("samples")
public class SampleController {

    // Use our own generated SampleBuilder
    private static final List<Sample> SAMPLES = Arrays.asList(
            new SampleBuilder().name("sunny").foo(1).build(),
            new SampleBuilder().name("bob").foo(2).build());

    @GetMapping
    public Sample getSample(@RequestParam String name) {
        return SAMPLES.stream()
                .filter(s -> s.getName().equals(name)) // and getter methods
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found by name:" + name));
    }
}

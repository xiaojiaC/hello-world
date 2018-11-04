package ${package}.${artifactId}.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 控制器示例。
 *
 * @author xiaojia
 * @since 1.0
 */
@Controller
@RequestMapping("demos")
public class DemoController {

    @GetMapping("welcome")
    public String sayHello(Model model) {
        model.addAttribute("nickname", "Bob");
        return "hello";
    }
}

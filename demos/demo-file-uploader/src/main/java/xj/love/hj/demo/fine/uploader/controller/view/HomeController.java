package xj.love.hj.demo.fine.uploader.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 视图控制器
 *
 * @author xiaojia
 * @since 1.0
 */
@Controller
@RequestMapping
public class HomeController {

    @GetMapping("index")
    public String index() {
        return "index";
    }

    @GetMapping("webUploader")
    public String webUploader() {
        return "webuploader";
    }

    @GetMapping("fineUploader")
    public String fineUploader() {
        return "fine-uploader";
    }
}

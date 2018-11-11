package xj.love.hj.demo.hello.java.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FreeMarker工具类，渲染数据到指定模板
 *
 * @author xiaojia
 * @since 1.0
 * @see <a href="http://freemarker.foofun.cn/pgui.html">freemarker</a>
 */
public class FreeMarkerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreeMarkerUtil.class);

    private static final String TEMPLATE_DIR = "/freemarker";
    private static final String TEMPLATE_PATH_FORMAT = "/%s/%s.ftl";
    private static final String DATA_MODEL = "dataModel";
    private static Configuration configuration;

    static {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassLoaderForTemplateLoading(FreeMarkerUtil.class.getClassLoader(), TEMPLATE_DIR);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration = cfg;
    }

    public static String processTeachers(List<Object> teachers) {
        Map<String, List<Object>> dataModel = new HashMap<String, List<Object>>();
        dataModel.put(DATA_MODEL, teachers);
        try {
            // 获取模板
            String templatePath = String.format(TEMPLATE_PATH_FORMAT, "teacher", "basic_info");
            Template template = configuration.getTemplate(templatePath);
            // 将数据渲染到模板
            StringWriter out = new StringWriter();
            template.process(dataModel, out);
            return out.toString();
        } catch (IOException | TemplateException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}

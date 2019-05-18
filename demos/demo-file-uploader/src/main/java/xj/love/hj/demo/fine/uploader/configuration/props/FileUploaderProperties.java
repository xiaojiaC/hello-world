package xj.love.hj.demo.fine.uploader.configuration.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传者配置属性
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.uploader")
public class FileUploaderProperties {

    /**
     * 文件存放目录
     */
    private String dir;

}

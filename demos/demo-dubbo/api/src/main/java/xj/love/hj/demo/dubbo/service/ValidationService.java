package xj.love.hj.demo.dubbo.service;

import javax.validation.constraints.Min;
import xj.love.hj.demo.dubbo.model.User;

/**
 * 验证功能服务。
 *
 * @author xiaojia
 * @since 1.0
 */
public interface ValidationService {

    /**
     * 与方法同名接口，首字母大写，用于区分验证场景。
     * 如：@NotNull(groups = ValidationService.Save.class)，可选
     */
    @interface Save {}
    void save(User user); // 验证参数不为空

    void delete(@Min(1) int id); // 直接对基本类型参数验证

}

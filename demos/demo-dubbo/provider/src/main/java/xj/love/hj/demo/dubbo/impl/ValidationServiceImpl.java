package xj.love.hj.demo.dubbo.impl;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import xj.love.hj.demo.dubbo.model.User;
import xj.love.hj.demo.dubbo.service.ValidationService;

/**
 * 验证服务供应商实现。
 *
 * @author xiaojia
 * @since 1.0
 */
public class ValidationServiceImpl implements ValidationService {

    @Override
    public void save(@NotNull User user) {
        System.out.println("save success ...");
    }

    @Override
    public void delete(@Min(1) int id) {
        System.out.println("delete success ...");
    }

}

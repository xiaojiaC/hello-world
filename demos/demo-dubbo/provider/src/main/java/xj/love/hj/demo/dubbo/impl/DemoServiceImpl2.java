package xj.love.hj.demo.dubbo.impl;

import xj.love.hj.demo.dubbo.service.DemoService;

/**
 * Demo服务第二种供应商实现。
 *
 * @author xiaojia
 * @since 1.0
 */
public class DemoServiceImpl2 implements DemoService {

    @Override
    public String sayHello(String name) {
        return "Welcome " + name;
    }

}

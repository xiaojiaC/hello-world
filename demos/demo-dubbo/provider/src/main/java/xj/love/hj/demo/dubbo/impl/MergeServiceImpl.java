package xj.love.hj.demo.dubbo.impl;

import java.util.ArrayList;
import java.util.List;
import xj.love.hj.demo.dubbo.service.MergeService;

/**
 * 分组合并菜单服务第一种供应商实现。
 *
 * @author xiaojia
 * @since 1.0
 */
public class MergeServiceImpl implements MergeService {

    @Override
    public List<String> mainMenus() {
        List<String> menus = new ArrayList<>();
        menus.add("menu-1");
        return menus;
    }

    @Override
    public List<String> subMenus() {
        List<String> menus = new ArrayList<>();
        menus.add("menu-1.1");
        menus.add("menu-1.2");
        return menus;
    }
}

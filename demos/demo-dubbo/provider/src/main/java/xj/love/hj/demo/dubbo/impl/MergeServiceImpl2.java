package xj.love.hj.demo.dubbo.impl;

import java.util.ArrayList;
import java.util.List;
import xj.love.hj.demo.dubbo.service.MergeService;

/**
 * 分组合并菜单服务第二种供应商实现。
 *
 * @author xiaojia
 * @since 1.0
 */
public class MergeServiceImpl2 implements MergeService {

    @Override
    public List<String> mainMenus() {
        List<String> menus = new ArrayList<String>();
        menus.add("menu-2");
        return menus;
    }

    @Override
    public List<String> subMenus() {
        List<String> menus = new ArrayList<String>();
        menus.add("menu-2.1");
        menus.add("menu-2.2");
        return menus;
    }

}

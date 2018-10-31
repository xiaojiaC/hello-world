package xj.love.hj.demo.dubbo.service;

import java.util.List;

/**
 * 合并菜单服务。
 *
 * @author xiaojia
 * @since 1.0
 * @see <a href="http://dubbo.apache.org/zh-cn/docs/user/demos/group-merger.html">分组聚合</a>
 */
public interface MergeService {

    List<String> mainMenus();

    List<String> subMenus();

}

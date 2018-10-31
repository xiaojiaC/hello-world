package xj.love.hj.demo.dubbo.merger;

import com.alibaba.dubbo.rpc.cluster.Merger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 自定义菜单分组合并策略。
 *
 * @author xiaojia
 * @since 1.0
 */
public class CustomListMerger implements Merger<List<String>> {

    @Override
    @SuppressWarnings("unchecked")
    public List<String> merge(List<String>... items) {
        List<String> result = new ArrayList<>();
        for (List<String> item : items) {
            if (item != null && !item.isEmpty()) {
                result.addAll(item);
            }
        }
        result.sort(Comparator.naturalOrder());
        return result;
    }

}

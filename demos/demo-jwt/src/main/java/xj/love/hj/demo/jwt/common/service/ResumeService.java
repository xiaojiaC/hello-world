package xj.love.hj.demo.jwt.common.service;

import java.util.Optional;
import xj.love.hj.demo.jwt.common.po.ResumePo;

/**
 * 个人简历服务
 *
 * @author xiaojia
 * @since 1.0
 */
public interface ResumeService {

    /**
     * 根据账户id查询其简历信息
     *
     * @param accountId 账户id
     * @return 存在则返回个人简历对象，否则返回空
     */
    Optional<ResumePo> findByAccountId(long accountId);

}

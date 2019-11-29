package xj.love.hj.demo.jwt.common.service.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xj.love.hj.demo.jwt.common.dao.ResumeDao;
import xj.love.hj.demo.jwt.common.po.ResumePo;
import xj.love.hj.demo.jwt.common.service.ResumeService;

/**
 * 个人简历服务实现
 *
 * @author xiaojia
 * @since 1.0
 */
@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeDao resumeDao;

    @Override
    public Optional<ResumePo> findByAccountId(long accountId) {
        return resumeDao.findByAccountId(accountId);
    }
}

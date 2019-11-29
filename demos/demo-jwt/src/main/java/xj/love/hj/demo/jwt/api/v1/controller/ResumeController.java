package xj.love.hj.demo.jwt.api.v1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xj.love.hj.demo.jwt.api.v0.vo.ResumeVo;
import xj.love.hj.demo.jwt.common.constant.ApiConstants;
import xj.love.hj.demo.jwt.common.context.ApiThreadContext;
import xj.love.hj.demo.jwt.common.context.ApiVisitor;
import xj.love.hj.demo.jwt.common.po.ResumePo;
import xj.love.hj.demo.jwt.common.service.ResumeService;

/**
 * 个人简历资源控制器
 *
 * @author xiaojia
 * @since 1.0
 */
@RestController("v1ResumeController")
@RequestMapping(value = ApiConstants.API_V1_PREFIX + "/resume",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "个人简历服务", tags = "Resume.Manage")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @GetMapping
    @ApiOperation("获取账户简历")
    public ResumeVo getByAccountId() {
        ApiVisitor apiVisitor = ApiThreadContext.getApiVisitor();
        Optional<ResumePo> resume = resumeService.findByAccountId(apiVisitor.getAccountId());
        ResumeVo resumeVo = new ResumeVo();
        resume.ifPresent(r -> BeanUtils.copyProperties(r, resumeVo));
        return resumeVo;
    }
}

package xj.love.hj.demo.spring.jpa.dao.fragment;

import xj.love.hj.demo.spring.jpa.domain.User;

/**
 * 自定义用户DAO片段
 *
 * @author xiaojia
 * @since 1.0
 */
public interface CustomizedUserRepository {

    /**
     * 依据全名查询用户信息
     *
     * @param fullName firstName + "." + lastName, 例如: "bob.cui"
     * @return 如果传递全名格式错误，直接返回null，否则为具体用户
     */
    User findByFullName(String fullName);
}

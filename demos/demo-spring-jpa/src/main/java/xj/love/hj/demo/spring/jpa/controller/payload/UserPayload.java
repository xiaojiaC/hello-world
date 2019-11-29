package xj.love.hj.demo.spring.jpa.controller.payload;

import org.springframework.data.web.JsonPath;
import org.springframework.data.web.ProjectedPayload;

/**
 * 用户请求有效载荷
 *
 * @author xiaojia
 * @since 1.0
 */
@ProjectedPayload
public interface UserPayload {

    @JsonPath("$..firstName")
    String getFirstName();

    @JsonPath({"$.lastName", "$.user.lastName"})
    String getLastName();
}

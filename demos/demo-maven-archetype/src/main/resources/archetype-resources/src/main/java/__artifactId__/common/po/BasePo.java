package ${package}.${artifactId}.common.po;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 抽象持久化实体通用字段。
 *
 * @author xiaojia
 * @since 1.0
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BasePo extends IdGeneratorPo {

    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_TIME = "updateTime";
    public static final String DELETE_TIME = "deleteTime";

    @CreatedDate
    @Column(name = "createTime", nullable = false)
    private Date createTime;

    @LastModifiedDate
    @Column(name = "updateTime", nullable = false)
    private Date updateTime;

    @Column(name = "deleteTime")
    private Date deleteTime;
}

package xj.love.hj.demo.jwt.common.po;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 基础域实体
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BasePo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) unsigned")
    protected long id;

    @Column(name = "created_at")
    @CreatedDate
    protected Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    protected Date updatedAt;
}

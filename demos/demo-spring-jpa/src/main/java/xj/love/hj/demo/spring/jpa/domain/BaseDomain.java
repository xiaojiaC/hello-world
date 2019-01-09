package xj.love.hj.demo.spring.jpa.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;

/**
 * 基础域实体
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@MappedSuperclass
public abstract class BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) unsigned")
    protected Long id;
}

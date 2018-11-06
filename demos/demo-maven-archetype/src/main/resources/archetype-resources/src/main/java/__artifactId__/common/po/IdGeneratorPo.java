package ${package}.${artifactId}.common.po;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 抽象持久化实体通用{@link #id}主键。
 *
 * @author xiaojia
 * @since 1.0
 */
@MappedSuperclass
public abstract class IdGeneratorPo {

    public static final String ID = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iAutoID", nullable = false, insertable = false, updatable = false,
            columnDefinition = "int(11) unsigned")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

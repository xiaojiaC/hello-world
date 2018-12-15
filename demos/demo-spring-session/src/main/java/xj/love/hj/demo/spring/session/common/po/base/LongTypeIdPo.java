package xj.love.hj.demo.spring.session.common.po.base;

/**
 * {@link Long}类型Id的持久化实体，为了解决 {@link org.springframework.beans.BeanUtils#copyProperties(Object,
 * Object)} 拷贝时源实体{@link AbstractPo#id}泛型属性值拷贝丢失问题。
 *
 * <pre>
 * class AbstractPo {
 *
 *      // 生成的getter方法中，返回值类型为Serializable类型，跟目标实体id字段Long类型不匹配，拷贝失败
 *     public Serializable getId() {
 *          return this.id;
 *     }
 * }
 * </pre>
 *
 * @author xiaojia
 * @since 1.0
 */
public abstract class LongTypeIdPo extends AbstractPo<Long> {

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setCreatedBy(Long createdBy) {
        super.setCreatedBy(createdBy);
    }

    @Override
    public Long getCreatedBy() {
        return super.getCreatedBy();
    }

    @Override
    public void setUpdatedBy(Long updatedBy) {
        super.setUpdatedBy(updatedBy);
    }

    @Override
    public Long getUpdatedBy() {
        return super.getUpdatedBy();
    }
}

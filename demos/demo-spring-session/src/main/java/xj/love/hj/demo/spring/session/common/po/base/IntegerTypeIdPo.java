package xj.love.hj.demo.spring.session.common.po.base;

/**
 * {@link Integer}类型Id的持久化实体
 *
 * @author xiaojia
 * @since 1.0
 */
public abstract class IntegerTypeIdPo extends AbstractPo<Integer> {

    @Override
    public void setId(Integer id) {
        super.setId(id);
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setCreatedBy(Integer createdBy) {
        super.setCreatedBy(createdBy);
    }

    @Override
    public Integer getCreatedBy() {
        return super.getCreatedBy();
    }

    @Override
    public void setUpdatedBy(Integer updatedBy) {
        super.setUpdatedBy(updatedBy);
    }

    @Override
    public Integer getUpdatedBy() {
        return super.getUpdatedBy();
    }
}

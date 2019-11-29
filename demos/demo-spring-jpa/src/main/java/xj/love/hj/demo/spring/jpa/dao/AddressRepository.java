package xj.love.hj.demo.spring.jpa.dao;

import org.springframework.stereotype.Repository;
import xj.love.hj.demo.spring.jpa.dao.base.BaseRepository;
import xj.love.hj.demo.spring.jpa.domain.Address;

/**
 * 地址实体DAO
 *
 * @author xiaojia
 * @since 1.0
 */
@Repository
public interface AddressRepository extends BaseRepository<Address, Long> {
}

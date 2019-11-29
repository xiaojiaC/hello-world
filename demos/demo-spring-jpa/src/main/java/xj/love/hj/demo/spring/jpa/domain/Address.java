package xj.love.hj.demo.spring.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 地址实体
 *
 * @author xiaojia
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dsj_address")
public class Address extends BaseDomain {

    @Column(name = "country", length = 25)
    private String country;

    @Column(name = "province", length = 25)
    private String province;

    @Column(name = "city", length = 25)
    private String city;

    @Column(name = "district", length = 50)
    private String district;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(name = "longitude", length = 64)
    private String longitude;

    @Column(name = "latitude", length = 64)
    private String latitude;
}

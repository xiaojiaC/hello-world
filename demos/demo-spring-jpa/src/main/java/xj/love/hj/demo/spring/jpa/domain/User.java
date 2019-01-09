package xj.love.hj.demo.spring.jpa.domain;

import java.util.Arrays;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import xj.love.hj.demo.spring.jpa.domain.event.UserCreatedEvent;

/**
 * 用户实体
 *
 * @author xiaojia
 * @since 1.0
 */
@Slf4j
@Data
@ToString(callSuper = true, exclude = "address")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dsj_user")
@NamedQuery(name = "User.findByAddressCity",
        query = "select u from User u left join Address a on u.addressId = a.id where a.city = ?1")
@NamedEntityGraph(name = "User.detail",
        attributeNodes = {@NamedAttributeNode("address")})
public class User extends BaseDomain {

    @Column(name = "first_name", length = 20)
    private String firstName;

    @Column(name = "last_name", length = 20)
    private String lastName;

    @Column(name = "gender", columnDefinition = "tinyint(1) unsigned")
    private Integer gender;

    @Column(name = "age", columnDefinition = "int(11) unsigned")
    private Integer age;

    @Column(name = "address_id", columnDefinition = "int(11) unsigned")
    private Long addressId;

    @OneToOne
    @JoinColumn(name = "address_id", insertable = false, updatable = false)
    private Address address;

    @Column(name = "tags")
    private String tags;

    @DomainEvents
    Collection<UserCreatedEvent> domainEvents() {
        return Arrays.asList(new UserCreatedEvent(firstName, lastName));
    }

    @AfterDomainEventPublication
    void callbackMethod() {
        log.info("Domain event publish after callback ...");
    }
}

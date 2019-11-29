package xj.love.hj.demo.hello.java.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import java.util.Date;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 人实体。
 *
 * @author xiaojia
 * @since 1.0
 */
@JsonSerialize(include = Inclusion.NON_EMPTY)
public class Person {

    @JsonProperty("name")
    private String name;

    @JsonIgnore
    private int age;

    // Unix timestamp, unit: ms
    @JsonProperty("birth_day")
    private Date birthday;

    @JsonProperty("profession")
    private String[] profession;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String[] getProfession() {
        return profession;
    }

    public void setProfession(String[] profession) {
        this.profession = profession;
    }

    @Override
    public String toString() {
        try {
            return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        } catch (Exception e) {
            return super.toString();
        }
    }
}

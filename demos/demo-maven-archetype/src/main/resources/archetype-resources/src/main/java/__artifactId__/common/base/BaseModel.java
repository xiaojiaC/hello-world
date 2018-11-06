package ${package}.${artifactId}.common.base;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 基础模型对象，内部封装了默认的多行风格的{@code toString()}实现。
 *
 * @author xiaojia
 * @since 1.0
 */
public abstract class BaseModel {

    /**
     * The multi line toString style. Using the {@code Person} example from {@link ToStringBuilder},
     * the output would look like this:
     *
     * <pre>
     * Person@182f0db[
     *   name=John Doe
     *   age=33
     *   smoker=false
     * ]
     * </pre>
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

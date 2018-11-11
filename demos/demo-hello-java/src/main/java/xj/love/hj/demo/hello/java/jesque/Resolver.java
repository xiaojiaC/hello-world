package xj.love.hj.demo.hello.java.jesque;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import net.greghaines.jesque.json.ObjectMapperFactory;

/**
 * 指示存放在Redis中的Jesque Job如何被解决执行。
 *
 * @author xiaojia
 * @since 1.0
 */
public class Resolver implements Runnable {

    private String className;
    private String methodName;
    private List<String> methodParamTypes;
    private List<?> methodArgs;

    public Resolver(String className, String methodName, List<String> methodParamTypes,
            List<?> methodArgs) {
        super();
        this.className = className;
        this.methodName = methodName;
        this.methodParamTypes = methodParamTypes;
        this.methodArgs = methodArgs;
    }

    @Override
    public void run() {
        try {
            Class<?> c = Class.forName(className);
            Object obj = c.getDeclaredConstructor().newInstance();
            if (methodParamTypes == null || methodParamTypes.size() == 0) {
                Method method = c.getMethod(methodName);
                method.invoke(obj);
            } else {
                Class<?>[] parameterTypes = new Class<?>[methodParamTypes.size()];
                Object[] args = new Object[methodArgs.size()];
                for (int i = 0; i < methodParamTypes.size(); i++) {
                    parameterTypes[i] = Class.forName(methodParamTypes.get(i));
                    String src = ObjectMapperFactory.get().writeValueAsString(methodArgs.get(i));
                    args[i] = ObjectMapperFactory.get().readValue(src, parameterTypes[i]);
                }
                Method method = c.getMethod(methodName, parameterTypes);
                method.invoke(obj, args);
            }
        } catch (SecurityException | IllegalArgumentException | ReflectiveOperationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Resolver [className=" + className + ", methodName=" + methodName
                + ", methodParamTypes="
                + methodParamTypes + ", methodArgs=" + methodArgs + "]";
    }
}

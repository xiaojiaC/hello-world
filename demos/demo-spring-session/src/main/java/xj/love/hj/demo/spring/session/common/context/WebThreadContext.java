package xj.love.hj.demo.spring.session.common.context;

/**
 * Web线程上下文，用于寄存访问者信息
 *
 * @author xiaojia
 * @since 1.0
 */
public class WebThreadContext {

    private static final ThreadLocal<Visitor> VISITOR = new ThreadLocal<>();

    public static void setVisitor(Visitor visitor) {
        VISITOR.set(visitor);
    }

    public static Visitor getVisitor() {
        return VISITOR.get();
    }
}

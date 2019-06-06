package xj.love.hj.demo.jvmti.agent.advice;

import java.lang.reflect.Method;
import net.bytebuddy.asm.Advice;

/**
 * 执行耗时前置/后置通知
 *
 * @author xiaojia
 * @since 1.0
 */
public class TimingAdvice {

    @Advice.OnMethodEnter
    public static long enter() {
        return System.currentTimeMillis();
    }

    @Advice.OnMethodExit
    public static void exit(@Advice.Origin Method method, @Advice.Enter long start) {
        System.out.println("TimingAdvice: " + method + " took "
                + (System.currentTimeMillis() - start) + " ms");
    }
}

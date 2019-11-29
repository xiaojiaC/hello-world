package xj.love.hj.demo.jvmti.agent;

import java.lang.instrument.Instrumentation;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import xj.love.hj.demo.jvmti.agent.advice.TimingAdvice;
import xj.love.hj.demo.jvmti.agent.interceptor.TimingInterceptor;

/**
 * JavaAgent入口
 *
 * @author xiaojia
 * @since 1.0
 */
public class TimerAgent {

    /**
     * 启动时动态加载agent
     *
     * delegation: ... -> handleInternal -> intercept -> ...
     *
     * @param agentOps agent参数
     * @param instrumentation 可以用独立于应用程序之外的代理（agent）程序来监测和协助运行在JVM上的应用程序。
     * 这种监测和协助包括但不限于获取JVM运行时状态，替换和修改类定义等
     */
    public static void premain(String agentOps, Instrumentation instrumentation) {
        System.out.println("Timer agent premain startup ...");
        new AgentBuilder.Default()
                .type(ElementMatchers.nameEndsWith("RequestMappingHandlerAdapter"))
                .transform((builder, typeDescription, classLoader, module) ->
                        builder.method(ElementMatchers.named("handleInternal"))
                                .intercept(MethodDelegation.to(TimingInterceptor.class))
                )
                .installOn(instrumentation);
    }

    /**
     * 启动后动态加载agent
     *
     * advice: ... -> enter + handleInternal + exit -> ...
     *
     * @param agentArgs agent参数
     * @param instrumentation 可以用独立于应用程序之外的代理（agent）程序来监测和协助运行在JVM上的应用程序。
     * 这种监测和协助包括但不限于获取JVM运行时状态，替换和修改类定义等
     */
    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("Timer agent main startup ...");
        new AgentBuilder.Default()
                .disableClassFormatChanges()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.nameEndsWith("RequestMappingHandlerAdapter"))
                .transform((builder, typeDescription, classLoader, module) ->
                        builder.visit(Advice.to(TimingAdvice.class)
                                .on(ElementMatchers.named("handleInternal"))))
                .installOn(instrumentation);
    }
}

package xj.love.hj.demo.hello.java.spring;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理模式：为另一个对象提供一个替身或占位符以控制对这个对象的访问。
 *
 * 基于cglib的动态代理
 *
 * @author xiaojia
 * @since 1.0
 */
public class CglibProxy {

    /**
     * 欢迎服务类
     */
    private static class GreetService {

        GreetService() {
        }

        void print(String name) {
            System.out.println(String.format("welcome %s!", name));
        }

    }

    /**
     * 欢迎服务类代理
     */
    private static class GreetServiceProxy implements MethodInterceptor {

        private Object target; // 被代理对象

        private GreetServiceProxy(Object target) {
            this.target = target;
        }

        @Override
        public Object intercept(Object enhancedObject, Method method, Object[] args,
                MethodProxy methodProxy)
                throws Throwable {
            System.out.println("source method: " + method.getName());
            method.invoke(this.target, args); // 直接调用原超类的方法

            System.out.println("proxy start ...");
            Object result = methodProxy.invokeSuper(enhancedObject, args); //代理类调用原超类的方法
            System.out.println("proxy end ...\n");

            return result;
        }

        private Object getProxyObject() {
            Enhancer enhancer = new Enhancer(); // 增强器，即动态代码生成器
            enhancer.setSuperclass(this.target.getClass()); // 设置生成的类将继承的超类
            enhancer.setCallback(this);  // 回调方法
            return enhancer.create(); // 动态生成字节码并返回代理对象
        }

    }

    public static void main(String[] args) {
        GreetService service = new GreetService();
        GreetServiceProxy serviceProxy = new GreetServiceProxy(service);

        GreetService greetService = (GreetService) serviceProxy.getProxyObject();
        greetService.print("bob");
    }
}

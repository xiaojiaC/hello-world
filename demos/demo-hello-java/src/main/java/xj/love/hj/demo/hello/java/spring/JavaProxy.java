package xj.love.hj.demo.hello.java.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理模式：为另一个对象提供一个替身或占位符以控制对这个对象的访问。
 *
 * 基于Java接口的动态代理
 *
 * @author xiaojia
 * @since 1.0
 */
public class JavaProxy {

    /**
     * 欢迎服务接口
     */
    private interface GreetService {

        void print(String name);
    }

    /**
     * 欢迎服务接口实现
     */
    private static class GreetServiceImpl implements GreetService {

        @Override
        public void print(String name) {
            System.out.println(String.format("welcome %s!", name));
        }
    }

    /**
     * 欢迎服务实现代理
     */
    private static class GreetServiceProxy implements InvocationHandler {

        private Object target; // 被代理对象

        private GreetServiceProxy(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println(proxy.getClass().getName());

            System.out.println("proxy start ...");
            Object result = method.invoke(target, args);
            System.out.println("proxy end ...\n");

            return result;
        }

        /**
         * 感觉生成的动态代理类实现类似于: {@link ProxyImpl}
         * <pre>
         * newProxyInstance方法：
         * 前两个参数[ClassLoader loader, Class<?>[] interfaces]用来在运行时动态生成代理类，其内部包含一个InvocationHandler单参构造器
         * 第三个参数[InvocationHandler h], 用来反射执行单参构造器动态生成代理类实例
         * </pre>
         */
        private Object getProxyObject() {
            return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                    target.getClass().getInterfaces(), this);
        }

    }

    public static void main(String[] args) {
        GreetService service = new GreetServiceImpl();
        GreetServiceProxy serviceProxy = new GreetServiceProxy(service);

        GreetService enhancedService = (GreetService) serviceProxy.getProxyObject();
        enhancedService.print("bob");

        ProxyImpl proxyImpl = new ProxyImpl(serviceProxy);
        proxyImpl.print("bob");
    }

    /**
     * 模拟代理实现
     */
    private static class ProxyImpl implements GreetService {

        private InvocationHandler handler;

        private ProxyImpl(InvocationHandler invocationHandler) {
            this.handler = invocationHandler;
        }

        @Override
        public void print(String name) {
            try {
                handler.invoke(handler, GreetServiceImpl.class.getMethod("print", String.class),
                        new Object[]{name});
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}

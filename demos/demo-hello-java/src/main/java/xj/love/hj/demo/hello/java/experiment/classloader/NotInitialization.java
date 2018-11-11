package xj.love.hj.demo.hello.java.experiment.classloader;

/**
 * 虚拟机规范定义有且只有5中情况必须立即对类进行初始化：
 * <pre>
 * - 使用new关键字实例化对象、读取或设置一个类的静态字段、调用一个类的静态方法
 * - 对类进行反射调用
 * - 初始化一个类发现其父类还没有进行初始化，需要先触发父类初始化
 * - 虚拟机启动市，用户指定的运行主类(包含main()方法)
 * - 当使用JDK1.7动态语言支持时
 *
 * - 但在初始化一个接口时，并不要求其父接口全部都完成初始化，只有在使用到父接口时才会初始化
 * </pre>
 *
 * @author xiaojia
 * @since 1.0
 */
public class NotInitialization {

    /**
     * 通过子类引用父类的静态字段，不会导致子类初始化
     */
    private static void testSubRefSuperStaticField() {
        System.out.println(SubClass.value);
    }

    /**
     * 通过数组定义来引用类，不会触发此类的初始化
     */
    private static void testArrayRef() {
        SuperClass[] sca = new SuperClass[10];
    }

    /**
     * 常量在编译阶段会存入调用类常量池中，本质上并没有直接引用到定义常量的类，因此不会触发定义常量的类的初始化
     */
    private static void testStaticFinalField() {
        System.out.println(ConstClass.TEST);
    }

    public static void main(String[] args) {
        testSubRefSuperStaticField();
        testArrayRef();
        testStaticFinalField();
    }
}

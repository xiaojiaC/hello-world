package xj.love.hj.demo.ast.test;

import com.sun.tools.attach.VirtualMachine;
import java.io.File;
import net.bytebuddy.agent.ByteBuddyAgent;

/**
 * 启动后Agent加载器
 *
 * @author xiaojia
 * @since 1.0
 */
public class AgentLoader {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println(
                    "Usage: java xj.love.hj.demo.ast.test.AgentLoader <appName> <agent> [options]");
            System.exit(0);
        }

        String attachAppName = args[0];
        String agentJar = args[1];
        String options = args.length > 2 ? args[2] : null;

        VirtualMachine.list().stream()
                .filter(jvm -> jvm.displayName().contains(attachAppName))
                .findAny()
                .ifPresent(jvm -> ByteBuddyAgent.attach(new File(agentJar), jvm.id(), options));
    }
}

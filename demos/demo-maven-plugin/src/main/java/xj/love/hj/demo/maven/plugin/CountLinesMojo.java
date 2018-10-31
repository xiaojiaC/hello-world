package xj.love.hj.demo.maven.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 统计代码行插件。
 *
 * <pre>
 * 测试命令: {@code ./mvnw xj.love.hj:demo-maven-plugin:1.0.0-SNAPSHOT:count},{@code ./mvnw demo:count}
 * </pre>
 *
 * @author xiaojia
 * @goal count
 * @since 1.0
 */
// @Mojo(name = "count") // 可改用注解方式声明而不是javadoc
public class CountLinesMojo extends AbstractMojo {

    /**
     * 默认包含的文件扩展名
     */
    private static final String[] INCLUDES_DEFAULT = {"java", "xml", "properties"};

    /**
     * 基础文件夹
     *
     * @parameter property = "project.basedir"
     * @required
     * @readonly
     */
    private File baseDirectory;

    /**
     * Java源文件夹
     *
     * @parameter property = "project.build.sourceDirectory"
     * @required
     * @readonly
     */
    private File sourceDirectory;

    /**
     * Java测试源文件夹
     *
     * @parameter property = "project.build.testSourceDirectory"
     * @required
     * @readonly
     */
    private File testSourceDirectory;

    /**
     * 资源文件夹
     *
     * @parameter property = "project.build.resources"
     * @required
     * @readonly
     */
    private List<Resource> resources;

    /**
     * 测试资源文件夹
     *
     * @parameter property = "project.build.testResources"
     * @required
     * @readonly
     */
    private List<Resource> testResources;

    /**
     * 包含用于计数的文件类型
     *
     * @parameter
     */
    private String[] includes;

    /**
     * 统计代码行逻辑
     *
     * @throws MojoExecutionException MojoExecutionException
     * @throws MojoFailureException MojoFailureException
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (includes == null || includes.length == 0) {
            includes = INCLUDES_DEFAULT;
        }
        try {
            countByDirectory(sourceDirectory);
            countByDirectory(testSourceDirectory);
            for (Resource resource : resources) {
                countByDirectory(new File(resource.getDirectory()));
            }
            for (Resource testResource : testResources) {
                countByDirectory(new File(testResource.getDirectory()));
            }
        } catch (Exception e) {
            throw new MojoExecutionException("count failed:", e);
        }
    }

    /**
     * 统计某个目录下文件的代码行
     *
     * @param directory 目录
     * @throws IOException 文件异常
     */
    private void countByDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }
        List<File> collected = new ArrayList<>();
        collectFiles(collected, directory);
        int lines = 0;
        for (File sourceFile : collected) {
            lines += countLine(sourceFile);
        }
        String path = directory.getAbsolutePath()
                .substring(baseDirectory.getAbsolutePath().length());

        // 输出统计日志
        getLog().info(path + ": " + lines + " lines of code in " + collected.size() + " files");
    }

    /**
     * 递归获取文件列表
     *
     * @param collected 文件列表list
     * @param file 文件
     */
    private void collectFiles(List<File> collected, File file) {
        if (file.isFile()) {
            for (String include : includes) {
                if (file.getName().endsWith("." + include)) {
                    collected.add(file);
                    break;
                }
            }
        } else {
            for (File subDirectory : file.listFiles()) {
                collectFiles(collected, subDirectory);
            }
        }
    }

    /**
     * 读取文件的行数
     *
     * @param file 文件对象
     * @return line
     * @throws IOException 文件操作异常
     */
    private int countLine(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int line = 0;
        try {
            while (reader.ready()) {
                reader.readLine();
                line++;
            }
        } finally {
            reader.close();
        }
        return line;
    }

}

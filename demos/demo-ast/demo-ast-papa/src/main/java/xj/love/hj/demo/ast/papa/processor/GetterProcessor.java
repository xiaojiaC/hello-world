package xj.love.hj.demo.ast.papa.processor;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import xj.love.hj.demo.ast.papa.utils.MethodNameUtil;

/**
 * Getter注解处理器。
 *
 * @author xiaojia
 * @see xj.love.hj.demo.ast.papa.annotation.Getter
 * @since 1.0
 */
@SupportedAnnotationTypes("xj.love.hj.demo.ast.papa.annotation.Getter")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class GetterProcessor extends AbstractProcessor {

    private Messager messager; // 在编译期打日志
    private JavacTrees javacTrees; // 提供待处理的抽象语法树
    private TreeMaker treeMaker; // 封装创建AST节点的一些方法
    private Names names; // 提供创建标识符的方法

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.javacTrees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(annotation -> {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            elements.forEach(element -> {
                JCTree jcTree = javacTrees.getTree(element);
                jcTree.accept(new TreeTranslator() {

                    @Override
                    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                        List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();
                        for (JCTree tree : jcClassDecl.defs) {
                            if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
                                JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) tree;
                                jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
                            }
                        }

                        jcVariableDeclList.forEach(jcVariableDecl -> {
                            messager.printMessage(Diagnostic.Kind.NOTE,
                                    jcVariableDecl.getName() + " has been processed");
                            jcClassDecl.defs = jcClassDecl.defs
                                    .append(makeGetterMethodDecl(jcVariableDecl));
                        });

                        super.visitClassDef(jcClassDecl);
                    }
                });
            });
        });
        return true;
    }

    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(treeMaker.Return(
                treeMaker.Select(treeMaker.Ident(names.fromString("this")),
                        jcVariableDecl.getName())));
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC),
                getNewMethodName(jcVariableDecl.getName()),
                jcVariableDecl.vartype, List.nil(), List.nil(), List.nil(), body, null);
    }

    private Name getNewMethodName(Name name) {
        String varName = name.toString();
        return names.fromString("get" + MethodNameUtil.upperFirstChar(varName));
    }
}

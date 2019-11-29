package xj.love.hj.demo.ast.papa.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import xj.love.hj.demo.ast.papa.annotation.Builder;
import xj.love.hj.demo.ast.papa.utils.MethodNameUtil;

/**
 * 构造器处理者。
 *
 * @author xiaojia
 * @see Builder
 * @since 1.0
 */
@SupportedAnnotationTypes("xj.love.hj.demo.ast.papa.annotation.Builder")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BuilderProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer; // 创建新文件

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Builder.class);
        elements.forEach(element -> {
            if (element.getKind() != ElementKind.CLASS) {
                return;
            }

            PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(element);
            String packageName = packageElement.getQualifiedName().toString();

            TypeMirror typeMirror = element.asType();
            String builderClassName = element.getSimpleName() + "Builder";
            TypeSpec.Builder builderClass = TypeSpec.classBuilder(builderClassName)
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC);
            MethodSpec.Builder builderMethod = MethodSpec.methodBuilder("build")
                    .returns(TypeName.get(element.asType()))
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("$T instance = new $T()", TypeName.get(typeMirror),
                            TypeName.get(typeMirror));
            element.getEnclosedElements().forEach(field -> {
                if (field.getKind() == ElementKind.FIELD
                        && !field.getModifiers().contains(Modifier.STATIC)) {
                    String fieldName = field.getSimpleName().toString();

                    FieldSpec fieldSpec = FieldSpec.builder(TypeName.get(field.asType()),
                            fieldName, Modifier.PRIVATE).build();
                    builderClass.addField(fieldSpec);

                    MethodSpec methodSpec = MethodSpec.methodBuilder(fieldName)
                            .returns(ClassName.get(packageName, builderClassName))
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(TypeName.get(field.asType()), fieldName)
                            .addStatement("this.$L = $L", fieldName, fieldName)
                            .addStatement("return this")
                            .build();
                    builderClass.addMethod(methodSpec);

                    builderMethod.addStatement("instance.set$L(this.$L)",
                            MethodNameUtil.upperFirstChar(fieldName), fieldName);
                }
            });
            builderMethod.addStatement("return instance");
            builderClass.addMethod(builderMethod.build());

            makeClassDecl(packageName, builderClass.build());
        });
        return true;
    }

    private void makeClassDecl(String packageName, TypeSpec typeSpec) {
        try {
            JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
        } catch (IOException e) {
            messager.printMessage(Kind.ERROR,
                    typeSpec.name + " build failed, due to: " + e.getMessage());
        }
    }
}

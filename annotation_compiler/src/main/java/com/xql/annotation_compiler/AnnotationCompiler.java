package com.xql.annotation_compiler;

import com.google.auto.service.AutoService;
import com.xql.annotation.BindPath;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * @ClassName: AnnotationCompiler
 * @Description: 注解处理器
 * @CreateDate: 2021/9/10 9:29
 * @UpdateUser: RyanLiu
 */

/**
 * 生成java代码
 */
@AutoService(Processor.class)
public class AnnotationCompiler extends AbstractProcessor {
    //生成java文件的对象
    Filer filer;
    Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    /**
     * 用来声明当前注解处理器要处理的注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindPath.class.getCanonicalName());
        return types;
    }

    /**
     * 支持的java版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //将java抽象化  节点文件  包节点 ==> 包 PackageElement    类节点 ==> 类 TypeElement  方法节点 ==> 方法  ExecutableElement 成员变量节点 ==> 成员变量  VariableElement
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
        //把所有要加入到路由表中的类以 key value的方式存好
        Map<String, String> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) element;
            //获取到全类名
            String activityName = typeElement.getQualifiedName().toString();
            //获取到这个类的key
            String key = typeElement.getAnnotation(BindPath.class).key();
            map.put(key, activityName);
        }

        //去写文件
        if (map.size() > 0) {
            Writer writer = null;
            //生成文件
            try {
                String utilName = "ActivityUtil" + System.currentTimeMillis();
                //去生成java文件
                JavaFileObject sourceFile = filer.createSourceFile("com.xql.util." + utilName);
                writer = sourceFile.openWriter();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("package com.xql.util;\n");
                stringBuffer.append("import com.xql.arouter.ARouter;\n");
                stringBuffer.append("import com.xql.arouter.IRouter;\n");
                stringBuffer.append("public class " + utilName + " implements IRouter { \n");
                stringBuffer.append("@Override\n");
                stringBuffer.append("public void putActivity() {\n");
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String activityName = map.get(key);
                    stringBuffer.append("ARouter.getInstance().addActivity(\"" + key + "\"," + activityName + ".class);");
                }
                stringBuffer.append("}\n}");
                writer.write(stringBuffer.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (writer != null){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
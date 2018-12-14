package com.lehow.net.mock.processor;

import com.google.auto.service.AutoService;
import com.lehow.net.annotation.MockNet;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

@AutoService(Processor.class) @SupportedAnnotationTypes({
    "com.lehow.net.annotation.MockNet"
}) @SupportedSourceVersion(SourceVersion.RELEASE_7)

public class MockNetProcessor extends AbstractProcessor {

  private Filer mFiler; //文件相关的辅助类
  private Elements mElementUtils; //元素相关的辅助类
  private Messager mMessager; //日志相关的辅助类

  private boolean hasProcess = false;

  @Override public synchronized void init(ProcessingEnvironment processingEnvironment) {
    super.init(processingEnvironment);
    mFiler = processingEnv.getFiler();
    mElementUtils = processingEnv.getElementUtils();
    mMessager = processingEnv.getMessager();
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    info("==MockNetProcessor==" + Thread.currentThread());
    Set<? extends Element> mockNetElements =
        roundEnvironment.getElementsAnnotatedWith(MockNet.class);

    if (!hasProcess) {
      hasProcess = true;
    } else {//只处理一次
      return false;
    }
    StringBuilder stringBuilder = new StringBuilder();

    for (Element mockNetElement : mockNetElements) {
      info(mockNetElement.toString());
      String netPath = findNetMethodPath(mockNetElement);
      if (netPath == null) {
        info("MockNet 注解的方法不是 Retrofit注解的请求");
      }
      if (!netPath.isEmpty()) {
        //stringBuilder.append(netPath+ ",");
        stringBuilder.append("\"" + netPath + "\"" + ",");
      }
    }
    if (stringBuilder.length() > 0) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    info(stringBuilder.toString());
    MethodSpec getMockNetPath = MethodSpec.methodBuilder("getMockNetPaths")
        .addModifiers(Modifier.PUBLIC)
        .addModifiers(Modifier.STATIC)
        .returns(String[].class)
        .addStatement("return new String[]{$L}", stringBuilder.toString())
        .build();

    //flexEntityElement.getSimpleName() + "$$FlexEntity"
    TypeSpec mockedNetPaths = TypeSpec.classBuilder("MockedNetPaths")
        .addModifiers(Modifier.PUBLIC)
        .addMethod(getMockNetPath)
        .build();

    info("mockedNetPaths" + mockedNetPaths.toString());
    try {
      JavaFile.builder("com.lehow.net.mock", mockedNetPaths).build().writeTo(mFiler);
      //JavaFile.builder(packageName, mockedNetPaths).build().writeTo(mFiler);
    } catch (IOException e) {
      info("==error==");
      e.printStackTrace();
    }
    return false;
  }

  private String findNetMethodPath(Element mockNetElement) {
    GET get = mockNetElement.getAnnotation(GET.class);
    if (get != null) {
      return get.value();
    }
    POST post = mockNetElement.getAnnotation(POST.class);
    if (post != null) {
      return post.value();
    }
    PUT put = mockNetElement.getAnnotation(PUT.class);
    if (put != null) {
      return put.value();
    }
    DELETE delete = mockNetElement.getAnnotation(DELETE.class);
    if (delete != null) {
      return delete.value();
    }
    return "";
  }

  private void info(String msg) {
    mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
  }

  private void info(Element e, String msg, Object... args) {
    mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args), e);
  }
}

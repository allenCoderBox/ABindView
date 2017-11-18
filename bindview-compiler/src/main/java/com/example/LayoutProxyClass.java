package com.example;

import com.example.model.IdModel;
import com.example.model.note.impl.LayoutFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author husongzhen
 */
public class LayoutProxyClass {

    public static final String STRING = "$";
    /**
     * 类元素
     */
    public TypeElement mTypeElement;

    /**
     * 元素相关的辅助类
     */
    private Elements mElementUtils;

    /**
     * FieldViewBinding类型的集合
     */
    private LayoutBinding bindViews;

    public void setBindViews(LayoutBinding bindViews) {
        this.bindViews = bindViews;
    }


    public LayoutProxyClass(TypeElement mTypeElement, Elements mElementUtils) {
        this.mTypeElement = mTypeElement;
        this.mElementUtils = mElementUtils;
    }


    private static final String viewPackage = "android.widget";

    /**
     * proxytool.IProxy
     */
    public static final ClassName IPROXY = ClassName.get("com.allen.code.bindview_api", "ILayoutProxy");


    /**
     * android.view.View
     */
    public static final String SUFFIX = "$$Layout";

    /**
     * 用于生成代理类
     */
    public JavaFile generateProxy() {

        //生成public void inject(final T target, View root)方法
        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "activity", Modifier.FINAL);
        injectMethodBuilder.addStatement("activity.setContentView(R.layout.$N)", bindViews.getLayoutName());

        LayoutFile layoutFile = bindViews.getLayoutIds();
        bindId(injectMethodBuilder, layoutFile);
        // 添加以$$Proxy为后缀的类
        TypeSpec.Builder builder = TypeSpec.classBuilder(mTypeElement.getSimpleName() + SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                //添加父接口
                .addSuperinterface(ParameterizedTypeName.get(IPROXY, TypeName.get(mTypeElement.asType())))
                //把inject方法添加到该类中
                .addMethod(injectMethodBuilder.build());


        addConstant(layoutFile, builder);
        TypeSpec typeSpec = builder.build();
        //添加包名
        String packageName = mElementUtils.getPackageOf(mTypeElement).getQualifiedName().toString();
        //生成Java文件
        return JavaFile.builder(packageName, typeSpec).build();
    }

    private void addConstant(LayoutFile layoutFile, TypeSpec.Builder builder) {
        BindLayoutProcessor.messager.printMessage(Diagnostic.Kind.WARNING, layoutFile.toString());
        for (IdModel item : layoutFile.getIds()) {
            FieldSpec fieldSpec = FieldSpec.builder(ClassName.get(viewPackage, item.getClazz()), getFieldName(item))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build();
            builder.addField(fieldSpec);
        }
    }


    private String getFieldName(IdModel item) {
        return item.getId();
    }

    private void bindId(MethodSpec.Builder injectMethodBuilder, LayoutFile layoutFile) {
        BindLayoutProcessor.messager.printMessage(Diagnostic.Kind.WARNING, layoutFile.toString());
        for (IdModel item : layoutFile.getIds()) {
            bindViewId(injectMethodBuilder, item);
        }

    }

    private void bindViewId(MethodSpec.Builder injectMethodBuilder, IdModel item) {
        injectMethodBuilder.addStatement("$N = ($N)activity.findViewById(R.id.$N)", getFieldName(item), item.getClazz(), item.getId());
    }

    private String getViewIdName(IdModel item) {
        return item.getId() + "Id";
    }


}
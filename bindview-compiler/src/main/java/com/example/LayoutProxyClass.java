package com.example;

import com.example.model.IdModel;
import com.example.model.LayoutModel;
import com.example.utils.CodeCheck;
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

        LayoutModel models = bindViews.getLayoutIds();
        bindId(injectMethodBuilder, models);
        // 添加以$$Proxy为后缀的类
        TypeSpec.Builder builder = TypeSpec.classBuilder(mTypeElement.getSimpleName() + SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                //添加父接口
                .addSuperinterface(ParameterizedTypeName.get(IPROXY, TypeName.get(mTypeElement.asType())))
                //把inject方法添加到该类中
                .addMethod(injectMethodBuilder.build());


        addConstant(models, builder);
        TypeSpec typeSpec = builder.build();
        //添加包名
        String packageName = mElementUtils.getPackageOf(mTypeElement).getQualifiedName().toString();
        //生成Java文件
        return JavaFile.builder(packageName, typeSpec).build();
    }

    private void addConstant(LayoutModel models, TypeSpec.Builder builder) {
        String resurceId = resetSourceId(models);
        BindLayoutProcessor.messager.printMessage(Diagnostic.Kind.WARNING, CodeCheck.isNotNullString(resurceId) ? resurceId : "");
        for (IdModel item : models.getIds()) {
            FieldSpec fieldSpec = FieldSpec.builder(ClassName.get(viewPackage, item.getClazz()), getFieldName(resurceId, item))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build();
            builder.addField(fieldSpec);
        }
        for (LayoutModel item : models.getChilds()) {
            addConstant(item, builder);
        }
    }

    private String resetSourceId(LayoutModel models) {
        return models.getSourceId();
    }

    private String getFieldName(String resurceId, IdModel item) {
        return CodeCheck.isNotNullString(resurceId) ? resurceId + STRING + item.getId() : item.getId();
    }

    private void bindId(MethodSpec.Builder injectMethodBuilder, LayoutModel models) {
        String resurceId = resetSourceId(models);
        BindLayoutProcessor.messager.printMessage(Diagnostic.Kind.WARNING, CodeCheck.isNotNullString(resurceId) ? resurceId : "");
        for (IdModel item : models.getIds()) {
            bindViewId(injectMethodBuilder, resurceId, item);
        }
        for (LayoutModel item : models.getChilds()) {
            bindId(injectMethodBuilder, item);
        }
    }

    private void bindViewId(MethodSpec.Builder injectMethodBuilder, String resurceId, IdModel item) {
        if (resurceId == null || "".equals(resurceId)) {
            injectMethodBuilder.addStatement("$N = ($N)activity.findViewById(R.id.$N)", getFieldName(resurceId, item), item.getClazz(), item.getId());
        } else {
            injectMethodBuilder.addStatement("$N = ($N)$N.findViewById(R.id.$N)", getFieldName(resurceId, item), item.getClazz(), resurceId, item.getId());
        }
    }

    private String getViewIdName(IdModel item) {
        return item.getId() + "Id";
    }


}
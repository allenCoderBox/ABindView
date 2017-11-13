package com.allen.code.bindview_api;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

public class ABindViewTool {

    public static final String SUFFIX = "$$Layout";

    //Activity
    @UiThread
    public static void bind(@NonNull Activity target) {
        createBinding(target);
    }


    public static void createBinding(@NonNull Object target) {
        try {
            //生成类名+后缀名的代理类，并执行注入操作  
            Class<?> targetClass = target.getClass();
            Class<?> proxyClass = Class.forName(targetClass.getName() + SUFFIX);
            ILayoutProxy proxy = (ILayoutProxy) proxyClass.newInstance();
            proxy.inject(target);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}  
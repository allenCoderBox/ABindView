package com.example;

import com.example.model.LayoutModel;
import com.example.utils.LayoutType;
import com.example.xml.LayoutParser;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

/**
 * Created by husongzhen on 17/11/10.
 * <p>
 * 读取xml
 * <p>
 * 怎么解决view include 同名的问题呢
 * 如果include有id， 生成一个对应的类
 * 没有id， 直接给顶层生成
 * 有没有简单的解决办法来解决id的问题
 * merge
 * veiwstub
 */

public class LayoutBinding {


    private final String layoutName;
    private final LayoutModel rooLayout;


    public LayoutBinding(Messager messager, Element element) {
        LayoutId layoutId = element.getAnnotation(LayoutId.class);
        layoutName = layoutId.value();
        String path = layoutId.path();
        LayoutParser layoutParser = new LayoutParser(path);
        rooLayout = new LayoutModel(LayoutType.layout);
        layoutParser.parserXml(layoutName, rooLayout);

    }


    public String getLayoutName() {
        return layoutName;
    }


    public LayoutModel getLayoutIds() {
        return rooLayout;
    }
}

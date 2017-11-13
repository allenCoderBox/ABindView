package com.example;

import com.example.model.LayoutModel;
import com.example.utils.LayoutType;
import com.example.xml.LayoutParser;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

/**
 * Created by husongzhen on 17/11/10.
 * <p>
 * <p>
 * 读取xml
 * <p>
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
        rooLayout.setLayoutName(layoutName);
        layoutParser.parserXml(rooLayout);
    }


    public String getLayoutName() {
        return layoutName;
    }


    public LayoutModel getLayoutIds() {
        return rooLayout;
    }
}

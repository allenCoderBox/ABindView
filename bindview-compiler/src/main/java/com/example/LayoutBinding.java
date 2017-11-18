package com.example;

import com.example.model.note.impl.LayoutFile;
import com.example.xml.XmlParser;
import com.example.xml.XmlParserImpl;

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
    private final LayoutFile file;


    public LayoutBinding(Messager messager, Element element) {
        LayoutId layoutId = element.getAnnotation(LayoutId.class);
        layoutName = layoutId.value();
        String path = layoutId.path();
        XmlParser parser = new XmlParserImpl();
        file = new LayoutFile(layoutName, path);
        parser.parserXml(file);
        file.mergeIds();
    }


    public String getLayoutName() {
        return layoutName;
    }


    public LayoutFile getLayoutIds() {










        return file;
    }
}

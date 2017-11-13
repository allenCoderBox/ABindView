package com.example.model;

import com.example.utils.LayoutType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author husongzhen
 * @date 17/11/13
 */

public class LayoutModel {
    private Set<IdModel> ids = new HashSet<>();
    private String fileName;
    private LayoutType type;
    private List<LayoutModel> childs = new ArrayList<>();


    public LayoutModel(LayoutType type) {
        this.type = type;
    }

    public void addChildLayout(LayoutModel child) {
        childs.add(child);
    }


    public void setType(LayoutType type) {
        this.type = type;
    }

    public void add(String clazz, String id) {
        ids.add(new IdModel(clazz, id));
    }

    public void setLayoutName(String fileName) {
        this.fileName = fileName;
    }


    public String getFileName() {
        return fileName;
    }


    public Set<IdModel> getIds() {
        return ids;
    }


}

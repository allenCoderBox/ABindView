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
    private LayoutType type;
    private List<LayoutModel> childs = new ArrayList<>();
    private Set<IdModel> ids = new HashSet<>();
    private String includeId;
    private IdModel root;
    private LayoutModel parentModel;


    private String sourceId;


    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public LayoutModel getParentModel() {
        return parentModel;
    }

    public void setParentModel(LayoutModel parentModel) {
        this.parentModel = parentModel;
    }

    public IdModel getRoot() {
        return root;
    }

    public void setRoot(IdModel root) {
        this.root = root;
    }

    public LayoutModel(LayoutType type) {
        this.type = type;
    }

    public void addChildLayout(LayoutModel child) {
        childs.add(child);
    }

    public void add(String clazz, String id) {
        ids.add(new IdModel(clazz, id));
    }

    public String getIncludeId() {
        return includeId;
    }

    public void setIncludeId(String includeId) {
        this.includeId = includeId;
    }

    public Set<IdModel> getIds() {
        return ids;
    }


    public List<LayoutModel> getChilds() {
        return childs;
    }

    @Override
    public String toString() {
        return "LayoutModel{" +
                "type=" + type +
                ", childs=" + childs +
                ", ids=" + ids +
                ", includeId='" + includeId + '\'' +
                ", root=" + root +
                ", parentModel=" + parentModel +
                '}';
    }
}

package com.example.model.note;

import com.example.model.IdModel;
import com.example.utils.CodeCheck;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author husongzhen
 * @date 17/11/17
 */

public class StubFile implements IXmlNode {
    protected String name;
    protected String path;
    protected IdModel root;
    protected Set<IdModel> ids = new HashSet<>();
    protected List<IXmlNode> childs;


    public StubFile(String name, String path) {
        this.name = name;
        this.path = path;
        childs = new ArrayList<>();
    }


    @Override
    public void setRoot(IdModel model) {
        resetRoot(model);
    }

    protected void resetRoot(IdModel model) {
        if (CodeCheck.isNotNull(model)) {
            this.root = model;
            addId(root);
        }
    }

    @Override
    public File getFile() {
        File file = new File(getLayoutPath());
        String apath = file.getAbsolutePath();
        return new File(apath);
    }


    @Override
    public void addId(IdModel childId) {
        if (CodeCheck.isNotNull(childId)) {
            ids.add(childId);
        }
    }


    @Override
    public Set<IdModel> getIds() {
        return ids;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void addChildNode(IXmlNode child) {
        childs.add(child);
    }

    private String getLayoutPath() {
        return path + name + ".xml";
    }


    @Override
    public String toString() {
        return "LayoutFile{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", root=" + root +
                ", ids=" + ids +
                ", childs=" + childs +
                '}';
    }

    public List<IXmlNode> getChilds() {
        return childs;
    }
}

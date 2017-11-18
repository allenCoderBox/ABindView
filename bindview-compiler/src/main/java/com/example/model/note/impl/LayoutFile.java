package com.example.model.note.impl;

import com.example.model.note.IXmlNode;
import com.example.model.note.StubFile;

import java.util.List;

/**
 * @author husongzhen
 * @date 17/11/17
 */

public class LayoutFile extends StubFile implements IXmlNode {

    public LayoutFile(String name, String path) {
        super(name, path);
    }

    public void mergeIds() {
        mergeChild(childs);
    }

    private void mergeChild(List<IXmlNode> childs) {
        for (IXmlNode node : childs) {
            ids.addAll(node.getIds());
            mergeChild(node.getChilds());
        }
    }
}

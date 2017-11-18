package com.example.model.note.impl;

import com.example.model.IdModel;
import com.example.model.note.IXmlNode;
import com.example.model.note.StubFile;
import com.example.utils.CodeCheck;

/**
 * Created by husongzhen on 17/11/17.
 */

public class IncludeFile extends StubFile implements IXmlNode {


    private IdModel includeId;


    @Override
    public void setRoot(IdModel model) {
        if (CodeCheck.isNotNull(includeId)){
            model.setId(includeId.getId());
        }
        resetRoot(model);
    }

    public void setIncludeId(IdModel includeId) {
        if (!CodeCheck.isNotNull(includeId)) {
            return;
        }
        this.includeId = includeId;
    }

    public IncludeFile(String name, String path) {
        super(name, path);
    }
}

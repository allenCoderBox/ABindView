package com.example.model.note;

import com.example.model.IdModel;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by husongzhen on 17/11/17.
 */


public interface IXmlNode {
    List<IXmlNode> getChilds();

    void setRoot(IdModel model);

    File getFile();

    void addId(IdModel childId);


    public Set<IdModel> getIds();


    String getPath();


    void addChildNode(IXmlNode includeFile);
}

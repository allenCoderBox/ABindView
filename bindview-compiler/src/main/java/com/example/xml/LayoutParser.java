package com.example.xml;

import com.example.model.IdModel;
import com.example.model.LayoutModel;
import com.example.utils.LayoutType;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by husongzhen on 17/11/13.
 */

public class LayoutParser {
    private String path;

    private LayoutModel layoutModel;

    public LayoutParser(String path) {
        this.path = path;
    }

    public void parserXml(LayoutModel rooModel) {
        this.layoutModel = rooModel;
        parserFile(rooModel);
    }

    private void parserFile(LayoutModel rooModel) {
        File file = new File(getLayoutPath(rooModel.getFileName(), path));
        String apath = file.getAbsolutePath();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(new File(apath));
            org.w3c.dom.Element rootElement = document.getDocumentElement();
            parseNote(rootElement);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void childParse(NodeList nodes) {
        if (nodes == null || nodes.getLength() == 0) {
            return;
        }
        int size = nodes.getLength();
        for (int i = 0; i < size; i++) {
            parseNote(nodes.item(i));
        }
    }

    private void parseNote(Node note) {
        String name = note.getNodeName();
        if (clearNull(name)) {
            return;
        }
        NamedNodeMap map = note.getAttributes();
        if (map != null) {
            if ("include".equals(name)) {
                includeId(map);
            } else {
                viewId(name, map);
            }
        }
        childParse(note.getChildNodes());

    }

    private void includeId(NamedNodeMap map) {
        Node layout = map.getNamedItem("layout");
        if (layout == null) {
            return;
        }
        String value = layout.getNodeValue();
        String includeName = value.replace("@layout/", "");
        LayoutModel model = new LayoutModel(LayoutType.include);
        model.setLayoutName(includeName);
        LayoutParser parser = new LayoutParser(path);
        parser.parserXml(model);
        layoutModel.addChildLayout(model);
        if (getNodeID(map) == null) {
            Set<IdModel> list = layoutModel.getIds();
            list.addAll(model.getIds());
        }
    }

    private void viewId(String name, NamedNodeMap map) {
        Node item = getNodeID(map);
        if (item != null) {
            String id = item.getNodeValue();
            id = id.replace("@+id/", "");
            layoutModel.add(name, id);
        }
    }

    private Node getNodeID(NamedNodeMap map) {
        return map.getNamedItem("android:id");
    }


    private boolean clearNull(String name) {
        if ("#text".equals(name)) {
            return true;
        }
        return false;
    }


    private String getLayoutPath(String name, String path) {
        return path + name + ".xml";
    }
}

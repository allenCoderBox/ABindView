package com.example.xml;

import com.example.model.LayoutModel;
import com.example.utils.LayoutType;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

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

    public void parserXml(String name, LayoutModel rooModel) {
        this.layoutModel = rooModel;
        parserFile(name);
    }

    private void parserFile(String name) {
        File file = new File(getLayoutPath(name, path));
        String apath = file.getAbsolutePath();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(new File(apath));
            org.w3c.dom.Element rootElement = document.getDocumentElement();
            parseRoot(rootElement);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseRoot(Element rootElement) {
        String name = rootElement.getNodeName();
        if (clearNull(name)) {
            return;
        }
        NamedNodeMap map = rootElement.getAttributes();
        if (map != null) {
            if (layoutModel.getIncludeId() == null) {
                Node item = getNodeID(map);
                if (item != null) {
                    String id = item.getNodeValue();
                    id = getIdName(id);
                    layoutModel.add(name, id);
                }
            } else {
                if (layoutModel.getParentModel() != null) {
                    layoutModel.getParentModel().add(name, layoutModel.getIncludeId());
                }
            }
        }
        childParse(rootElement.getChildNodes());
    }

    private String getIdName(String id) {
        return id.replace("@+id/", "");
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
        String includeName = getLayoutName(value);
        LayoutParser parser = new LayoutParser(path);
        LayoutModel includeModel = new LayoutModel(LayoutType.include);
        Node item = getNodeID(map);
        if (item != null) {
            String id = item.getNodeValue();
            id = getIdName(id);
            includeModel.setIncludeId(id);
        }
        includeModel.setParentModel(layoutModel);
        parser.parserXml(includeName, includeModel);
        layoutModel.addChildLayout(includeModel);
    }

    private String getLayoutName(String value) {
        return value.replace("@layout/", "");
    }

    private void viewId(String name, NamedNodeMap map) {
        Node item = getNodeID(map);
        if (item != null) {
            String id = item.getNodeValue();
            id = getIdName(id);
            if (layoutModel.getIncludeId() != null) {
                layoutModel.add(name, id);
            } else {
                if (layoutModel.getParentModel() == null) {
                    layoutModel.add(name, id);
                } else {
                    layoutModel.getParentModel().add(name, id);
                }
            }
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

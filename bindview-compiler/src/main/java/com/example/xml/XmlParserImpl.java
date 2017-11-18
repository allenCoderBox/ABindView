package com.example.xml;

import com.example.model.IdModel;
import com.example.model.note.IXmlNode;
import com.example.model.note.impl.IncludeFile;
import com.example.utils.CodeCheck;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.tools.Diagnostic;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.example.BindLayoutProcessor.messager;

/**
 * Created by husongzhen on 17/11/18.
 */

public class XmlParserImpl implements XmlParser {
    @Override
    public IXmlNode parserXml(IXmlNode layoudFile) {
        Element rootElement = getRootElement(layoudFile);
        if (!CodeCheck.isNotNull(rootElement)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "root element is null error!");
            return layoudFile;
        }
        IdModel idModel = viewId(rootElement);
        layoudFile.setRoot(idModel);
        childParse(layoudFile, rootElement.getChildNodes());
        return layoudFile;
    }


    private void childParse(IXmlNode layoudFile, NodeList nodes) {
        if (nodes == null || nodes.getLength() == 0) {
            return;
        }
        int size = nodes.getLength();
        for (int i = 0; i < size; i++) {
            parseNote(layoudFile, nodes.item(i));
        }
    }

    private void parseNote(IXmlNode nodeFile, Node item) {
        String nodeName = item.getNodeName();
        if (clearNull(nodeName)) {
            return;
        }
        if ("include".equals(nodeName)) {
            includeId(nodeFile, item);
        } else {
            IdModel childId = viewId(item);
            nodeFile.addId(childId);
        }
    }


    private void includeId(IXmlNode nodeFile, Node node) {
        NamedNodeMap atrributes = node.getAttributes();
        Node layout = atrributes.getNamedItem("layout");
        if (!CodeCheck.isNotNull(layout)) {
            return;
        }
        String includeName = getChildFileName(layout);
        IncludeFile includeFile = new IncludeFile(includeName, nodeFile.getPath());
        includeFile.setIncludeId(viewId(node));
        parserXml(includeFile);
        nodeFile.addChildNode(includeFile);
    }

    private String getChildFileName(Node layout) {
        String value = layout.getNodeValue();
        return getLayoutName(value);
    }

    private String getLayoutName(String value) {
        return value.replace("@layout/", "");
    }

    protected IdModel viewId(Node node) {
        if (!CodeCheck.isNotNull(node)) {
            return null;
        }
        String name = node.getNodeName();
        NamedNodeMap atrributes = node.getAttributes();
        if (!CodeCheck.isNotNull(atrributes)) {
            return null;
        }

        Node item = getNodeID(atrributes);
        if (!CodeCheck.isNotNull(item)) {
            return null;
        }
        String id = item.getNodeValue();
        id = getIdName(id);

        return new IdModel(name, id);
    }

    private Node getNodeID(NamedNodeMap map) {
        return map.getNamedItem("android:id");
    }

    private String getIdName(String id) {
        return id.replace("@+id/", "");
    }


    private String getNodeName(Element node) {
        return node.getNodeName();
    }

    private Element getRootElement(IXmlNode xmlFile) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(xmlFile.getFile());
            org.w3c.dom.Element rootElement = document.getDocumentElement();
            return rootElement;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean clearNull(String name) {
        if ("#text".equals(name)) {
            return true;
        }
        return false;
    }
}

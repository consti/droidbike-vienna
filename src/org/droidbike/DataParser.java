package org.droidbike;

import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataParser {

    private static DocumentBuilderFactory factory;

    static {
        factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
    }

    private static Document getDocument(File data) {
        Document doc = null;
        try {
            doc = factory.newDocumentBuilder().parse(data);
        } catch (ParserConfigurationException e) {
            Log.e("DataParser", "error parsing XML document: " + e.getMessage());
        } catch (SAXException e) {
            Log.e("DataParser", "error parsing XML document: " + e.getMessage());
        } catch (IOException e) {
            Log.e("DataParser", "error parsing XML document: " + e.getMessage());
        }
        return doc;
    }

    public static List<RentShopLocation> parseData(File xmlData) {

        List<RentShopLocation> list = new ArrayList<RentShopLocation>();
        Document doc = getDocument(xmlData);

        NodeList rootnodes = doc.getDocumentElement().getElementsByTagName("stations");
        NodeList nodeList = rootnodes.item(0).getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            RentShopLocation location = new RentShopLocation();

            NodeList subNodes = node.getChildNodes();
            for (int j = 0; j < subNodes.getLength(); j++) {
                Node subNode = nodeList.item(i);
                String ln = subNode.getLocalName();
                if ("id".equals(ln)) {
                    location.id = Integer.valueOf(subNode.getTextContent());
                } else if ("internal_id".equals(ln)) {
                    location.internalId = Integer.valueOf(subNode.getTextContent());
                } else if ("name".equals(ln)) {
                    location.name = subNode.getTextContent();
                } else if ("boxes".equals(ln)) {
                    location.boxes = Integer.valueOf(subNode.getTextContent());
                } else if ("free_boxes".equals(ln)) {
                    location.freeBoxes = Integer.valueOf(subNode.getTextContent());
                } else if ("free_bikes".equals(ln)) {
                    location.freeBikes = Integer.valueOf(subNode.getTextContent());
                } else if ("status".equals(ln)) {
                    location.active = subNode.getTextContent().equals("aktiv");
                } else if ("description".equals(ln)) {
                    location.description = subNode.getTextContent();
                } else if ("latitude".equals(ln)) {
                    location.latitude = Float.valueOf(subNode.getTextContent());
                } else if ("longitude".equals(ln)) {
                    location.longitude = Float.valueOf(subNode.getTextContent());
                }


//                 <id>2005</id>
//<internal_id>1087</internal_id>
//<name>Millenium Tower</name>
//<boxes>35</boxes>
//<free_boxes>9</free_boxes>
//<free_bikes>24</free_bikes>
//<status>aktiv</status>
//−
//<description>
//Am Maria Restituta Platz zwischen Rivergate und Schnellbahn
//</description>
//<latitude>48.24167413865949</latitude>
//<longitude>16.384670734405518</longitude>
            }
        }

        return list;
    }

    public static void main(String[] args) {
          parseData(new File("./citybike.xml"));

    }


}

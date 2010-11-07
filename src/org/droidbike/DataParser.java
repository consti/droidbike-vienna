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

    private static Document getDocument(String data) {
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

    public static List<RentShopLocation> parseData(String xmlData) {

        List<RentShopLocation> list = new ArrayList<RentShopLocation>();
        Document doc = getDocument(xmlData);

        NodeList rootnodes = doc.getElementsByTagName("station");
        for (int i = 0; i < rootnodes.getLength(); i++) {
            Node node = rootnodes.item(i);
            RentShopLocation location = new RentShopLocation();
//            System.out.println(node.getNodeName());

            NodeList subNodes = node.getChildNodes();
            for (int j = 0; j < subNodes.getLength(); j++) {
                Node subNode = subNodes.item(j);
                String ln = subNode.getNodeName();
//                System.out.println("   " + ln);
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
            }
            list.add(location);
        }

        return list;
    }

//    public static void main(String[] args) {
//        parseData(new File("./citybike.xml"));
//
//    }


}

package org.droidbike;

import android.util.Log;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DataParser {

    private static DocumentBuilderFactory factory;

    static {
        factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
    }

    private static Document getDocument(InputStream data) {
        Document doc = null;
        try {
            doc = factory.newDocumentBuilder().parse(data, "UTF-8");
        } catch (ParserConfigurationException e) {
            Log.e("DataParser", "error parsing XML document: " + e.getMessage());
        } catch (SAXException e) {
            Log.e("DataParser", "error parsing XML document: " + e.getMessage());
        } catch (IOException e) {
            Log.e("DataParser", "error parsing XML document: " + e.getMessage());
        }
        return doc;
    }

    public static List<RentShopLocation> parseData(InputStream xmlData) {

        List<RentShopLocation> list = new ArrayList<RentShopLocation>();
        Document doc = getDocument(xmlData);

        NodeList rootnodes = doc.getElementsByTagName("station");
        for (int i = 0; i < rootnodes.getLength(); i++) {
            Node node = rootnodes.item(i);
            RentShopLocation location = new RentShopLocation();

            NodeList subNodes = node.getChildNodes();
            for (int j = 0; j < subNodes.getLength(); j++) {
                if (subNodes.item(j) instanceof Element) {
                    Element subNode = (Element) subNodes.item(j);
                    String ln = subNode.getNodeName();
                    Text textChild = (Text) subNode.getFirstChild();
                    String text = textChild != null ? textChild.getData() : "";

                    if ("id".equals(ln)) {
                        location.id = Integer.valueOf(text);
                    } else if ("internal_id".equals(ln)) {
                        location.internalId = Integer.valueOf(text);
                    } else if ("name".equals(ln)) {
                        location.name = text;
                    } else if ("boxes".equals(ln)) {
                        location.boxes = Integer.valueOf(text);
                    } else if ("free_boxes".equals(ln)) {
                        location.freeBoxes = Integer.valueOf(text);
                    } else if ("free_bikes".equals(ln)) {
                        location.freeBikes = Integer.valueOf(text);
                    } else if ("status".equals(ln)) {
                        location.active = "aktiv".equals(text);
                    } else if ("description".equals(ln)) {
                        location.description = text;
                    } else if ("latitude".equals(ln)) {
                        location.latitude = Float.valueOf(text);
                    } else if ("longitude".equals(ln)) {
                        location.longitude = Float.valueOf(text);
                    }
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

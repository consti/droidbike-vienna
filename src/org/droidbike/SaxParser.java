package org.droidbike;

import android.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaxParser {


    public static List<RentShopLocation> parseXmlFile(InputStream stream) {
        try {
             LocationsHandler handler = new LocationsHandler();

            // Create a builder factory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);

            // Create the builder and parse the file
            factory.newSAXParser().parse(stream, handler);
            return handler.getLocations();
        } catch (SAXException e) {
            Log.e("SaxParser", e.getMessage());
        } catch (ParserConfigurationException e) {
            Log.e("SaxParser", e.getMessage());
        } catch (IOException e) {
            Log.e("SaxParser", e.getMessage());
        }
        return null;
    }

    static class LocationsHandler extends DefaultHandler {

        private static List<String> validSubElementNames = Arrays.asList("id", "internal_id", "name", "boxes",
                "free_boxes", "free_bikes", "status", "description", "latitude", "longitude");

        List<RentShopLocation> locations = new ArrayList<RentShopLocation>(100);
        RentShopLocation currentLocation;
        String currentElementName;

        public List<RentShopLocation> getLocations() {
            return locations;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("station".equals(qName)) {
                currentLocation = new RentShopLocation();
            }

            if (validSubElementNames.contains(qName)) {
                currentElementName = qName;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("station".equals(qName)) {
                if (currentLocation != null && locations != null) {
                    locations.add(currentLocation);
                    currentLocation = null;
                }
            }

            if (validSubElementNames.contains(qName)) {
                currentElementName = null;
            }
        }

        @Override
        public void characters(char[] chars, int offset, int length) throws SAXException {
            String text = String.copyValueOf(chars, offset, length).trim();
            if (text.length() != 0 && currentLocation != null && currentElementName != null) {
                if ("id".equals(currentElementName)) {
                    currentLocation.id = Integer.valueOf(text);
                } else if ("internal_id".equals(currentElementName)) {
                    currentLocation.internalId = Integer.valueOf(text);
                } else if ("name".equals(currentElementName)) {
                    currentLocation.name = text;
                } else if ("boxes".equals(currentElementName)) {
                    currentLocation.boxes = Integer.valueOf(text);
                } else if ("free_boxes".equals(currentElementName)) {
                    currentLocation.freeBoxes = Integer.valueOf(text);
                } else if ("free_bikes".equals(currentElementName)) {
                    currentLocation.freeBikes = Integer.valueOf(text);
                } else if ("status".equals(currentElementName)) {
                    currentLocation.active = "aktiv".equals(text);
                } else if ("description".equals(currentElementName)) {
                    currentLocation.description = text;
                } else if ("latitude".equals(currentElementName)) {
                    currentLocation.latitude = Float.valueOf(text);
                } else if ("longitude".equals(currentElementName)) {
                    currentLocation.longitude = Float.valueOf(text);
                }
            }
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
            Log.e("SaxParser", e.getMessage());
            locations = null;
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            Log.e("SaxParser", e.getMessage());
            locations = null;
        }
    }
}

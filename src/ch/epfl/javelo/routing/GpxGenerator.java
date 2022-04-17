package ch.epfl.javelo.routing;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GpxGenerator {

    private GpxGenerator() {} //non-instantiable

    private static Document newDocument() {
        try {
            return DocumentBuilderFactory
                    .newDefaultInstance()
                    .newDocumentBuilder()
                    .newDocument();
        } catch (ParserConfigurationException e) {
            throw new Error(e); // Should never happen
        }
    }

    public Document createGPX(Route route, ElevationProfile profile) {

        Document doc = newDocument(); // voir plus bas

        Element root = doc
                .createElementNS("http://www.topografix.com/GPX/1/1",
                        "gpx");
        doc.appendChild(root);

        root.setAttributeNS(
                "http://www.w3.org/2001/XMLSchema-instance",
                "xsi:schemaLocation",
                "http://www.topografix.com/GPX/1/1 "
                        + "http://www.topografix.com/GPX/1/1/gpx.xsd");
        root.setAttribute("version", "1.1");
        root.setAttribute("creator", "JaVelo");

        Element metadata = doc.createElement("metadata");
        root.appendChild(metadata);

        Element name = doc.createElement("name");
        metadata.appendChild(name);
        name.setTextContent("Route JaVelo");

        Element rte = doc.createElement("rte");
        for (Edge edge: route.edges()) {
            for (int i = edge.fromNodeId(); i < edge.toNodeId(); i++) {
                Element rtept = doc.createElement("rtept");
                Element ele = doc.createElement("ele");
                ele.setAttribute("elevation", route.elevationAt());
            }
            }
            profile.
        }
        doc.appendChild(rte);

    }

    public void writeGPX() {

    }

}

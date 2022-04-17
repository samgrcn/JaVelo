package ch.epfl.javelo.routing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

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

    public static Document createGPX(Route route, ElevationProfile profile) {

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

        int position = 0;
        for (int i = 0; i < route.points().size() - 1; i++) {

            Element rtept = doc.createElement("rtept");
            Element ele = doc.createElement("ele");
            ele.setAttribute("ele", Double.toString(profile.elevationAt(position)));
            rtept.appendChild(ele);
            rte.appendChild(rtept);

            position += route.points().get(i).distanceTo(route.points().get(i + 1));
        }
        return doc;
    }


    public static void writeGPX(String fileName, Route route, ElevationProfile profile) throws IOException {

        Document doc = newDocument();
        Writer w = Files.newBufferedWriter(Path.of(fileName), Charset.defaultCharset());

        try {
            Transformer transformer = TransformerFactory
                    .newDefaultInstance()
                    .newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc),
                    new StreamResult(w));
        } catch (TransformerException e) {
            throw new Error(e); // Should never happen

        }
    }
}

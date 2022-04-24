package ch.epfl.javelo.routing;

import ch.epfl.javelo.projection.PointCh;
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
import java.util.Iterator;
import java.util.Locale;

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

    public static Document createGpx(Route route, ElevationProfile profile) {

        Document doc = newDocument();

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
        root.appendChild(rte);

        double position = 0;
        Iterator<Edge> iterator = route.edges().iterator();

        for (PointCh p: route.points()) {

            Element rtept = doc.createElement("rtept");
            Element ele = doc.createElement("ele");
            rtept.setAttribute("lat", String.format(Locale.ROOT, "%.5f", Math.toDegrees(p.lat())));
            rtept.setAttribute("lon", String.format(Locale.ROOT, "%.5f", Math.toDegrees(p.lon())));

            ele.setTextContent(String.format(Locale.ROOT, "%.2f", profile.elevationAt(position)));

            rtept.appendChild(ele);
            rte.appendChild(rtept);

            if(iterator.hasNext()) {
                position += iterator.next().length();
            }
        }

        return doc;
    }


    public static void writeGpx(String fileName, Route route, ElevationProfile profile) throws IOException {

        Document doc = createGpx(route, profile);
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

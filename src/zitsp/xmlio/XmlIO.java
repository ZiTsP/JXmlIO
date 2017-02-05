package zitsp.xmlio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XmlIO {
    
    private XmlIO() {
    }
    
    public static List<XmlElement> read(Path path) {
        if (path == null || !Files.exists(path) || Files.isDirectory(path)) {
            return new ArrayList<>();
        }
        List<XmlElement> datas = new ArrayList<>();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try (BufferedInputStream stream = new BufferedInputStream(Files.newInputStream(path))) {
            XMLStreamReader reader = factory.createXMLStreamReader(stream);
            while (reader.hasNext()) {
                read(reader).ifPresent(data -> datas.add(data));
                reader.next();
            }
            reader.close();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
        return datas;
    }
    
    private static Optional<XmlElement> read(XMLStreamReader reader) throws XMLStreamException {
        int eventType = reader.getEventType();
        if (eventType == XMLStreamConstants.START_ELEMENT) {
            XmlElement data = new XmlElement(reader.getLocalName());
            IntStream.range(0, reader.getAttributeCount())
                .forEach(e -> data.setAttribute(reader.getAttributeLocalName(e), reader.getAttributeValue(e)));
            reader.next();
            while (reader.hasNext()) {
                switch (reader.getEventType()) {
                case XMLStreamConstants.END_DOCUMENT :
                case XMLStreamConstants.END_ELEMENT :
                    return Optional.of(data);
                case XMLStreamConstants.START_ELEMENT :
                    read(reader).ifPresent(child -> data.setChild(child));
                    break;
                case XMLStreamConstants.CHARACTERS :
                    data.setElement(reader.getText());
                    break;
                default :
                    break;
                }
                reader.next();
            }
        }
        return Optional.empty();
    }
    

    private static String XML_HEADER = "<?xml version=\"1.0\" ?>";
    
    public static void write(Path path, List<XmlElement> datas) {
        if (datas == null || datas.isEmpty()) {
            return;
        }
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try (OutputStream stream = Files.newOutputStream(path);
                OutputStreamWriter writer = new OutputStreamWriter(stream)) {
            writer.write(XML_HEADER);
            for (XmlElement data : datas) {
                write(data, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void write(Path path, XmlElement... datas) {
        write(path, Arrays.asList(datas));
    }
    
    private static void write(XmlElement data, OutputStreamWriter writer) throws IOException {
        if (data != null) {
            if (data.isIndependentTag()) {
                writer.write(data.getIndependentTag().get());
                return;
            } else {
                writer.write(data.getStartTag());
                if (data.hasText()) {
                    writer.write(data.getText().get());
                }
                if (data.hasChild()) {
                    for (XmlElement child : data.getChildren()) {
                        write(child, writer);
                    }
                }
                writer.write(data.getEndTag());
            }
        }
    }
    
}

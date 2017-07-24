package com.flipsports.xml;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Slf4j
public class XmlReaderActor extends AbstractActor {
    public static Props props() {
        return Props.create(XmlReaderActor.class, XmlReaderActor::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(XmlData.class, this::readXmlFile)
                .build();
    }

    private void readXmlFile(XmlData xmlData) {
        log.info("Parsing XML file: {}", xmlData.getFile());
        log.info(this.getSelf().path().toString());

        val xmlInputFactory = XMLInputFactory.newInstance();
        try {
            validateXML(xmlData.file);

            val xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(xmlData.file));

            val processingLogic = ProcessingLogicRegistry.lookup(xmlData.getFile().getName(), getContext());

            parseXml(xmlEventReader, processingLogic);
        } catch (XMLStreamException | FileNotFoundException e) {
            throw new IllegalArgumentException(String.format("Could not load file: %s", xmlData.file), e);
        }
    }

    private void validateXML(File file) {
        //TODO add XSD validation throwing exception
    }

    private void parseXml(XMLEventReader xmlEventReader, ActorRef logic) throws XMLStreamException {
        try {
            while (xmlEventReader.hasNext()) {
                val xmlEvent = xmlEventReader.nextEvent();
                log.debug("parsed event: {}", getEventTypeString(xmlEvent.getEventType()));

                if (xmlEvent.isStartElement()) {
                    val startElement = xmlEvent.asStartElement();
                    log.debug("element: {}", startElement.getName());
                }
                logic.tell(xmlEvent, this.getSelf());
            }
        } catch (Exception e) {
            log.error("Exception occurred while processing XML", e);
            throw e;
        }
    }

    private String getEventTypeString(int eventType) {
        switch (eventType) {
            case 1:
                return "StartElementEvent";
            case 2:
                return "EndElementEvent";
            case 3:
                return "ProcessingInstructionEvent";
            case 4:
                return "CharacterEvent";
            case 5:
                return "CommentEvent";
            case 6:
            default:
                return "UNKNOWN_EVENT_TYPE";
            case 7:
                return "StartDocumentEvent";
            case 8:
                return "EndDocumentEvent";
            case 9:
                return "EntityReferenceEvent";
            case 10:
                return "AttributeBase";
            case 11:
                return "DTDEvent";
            case 12:
                return "CDATA";
        }
    }

    @Value
    public static class XmlData {
        private final File file;
    }
}
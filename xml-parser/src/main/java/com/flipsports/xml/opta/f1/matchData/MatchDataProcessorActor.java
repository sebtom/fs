package com.flipsports.xml.opta.f1.matchData;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.flipsports.utils.ActorLookupSupport;
import com.flipsports.utils.XmlSupport;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import static com.flipsports.SystemConfig.ACTOR_DATA_STORE;

@Slf4j
public class MatchDataProcessorActor extends AbstractActor implements XmlSupport, ActorLookupSupport {
    public static Props props() {
        return Props.create(MatchDataProcessorActor.class, MatchDataProcessorActor::new);
    }

    public final MatchDataTransformer transformer = new MatchDataTransformer();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MatchDataXml.class, this::parseEvents)
                .build();
    }

    private void parseEvents(MatchDataXml dataXml) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        try {
            Document document = buildDOMDocument(dataXml.getEvents());
            MatchData result = transformer.parseDOM(document);
            getContext().actorFor(top(ACTOR_DATA_STORE)).tell(result, this.getSelf());
        } catch (IOException | SAXException | ParserConfigurationException | XPathExpressionException e) {
            log.error("Could not process node {}", dataXml.getEvents().get(0), e);
            throw e;
        }
    }

    private Document buildDOMDocument(List<XMLEvent> events) throws IOException, SAXException, ParserConfigurationException {
        String xmlString = events.stream()
                .map(XMLEvent::toString)
                .collect(Collectors.joining());

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlString));
        return db.parse(is);

    }
}
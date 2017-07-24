package com.flipsports.xml.opta.f1.matchData;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.flipsports.utils.XmlSupport;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MatchDataProcessor extends AbstractActor implements XmlSupport {
    public static Props props() {
        return Props.create(MatchDataProcessor.class, MatchDataProcessor::new);
    }

    public static final DateTimeFormatter LAST_MODIFIED_PARSER = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxxx");
    public static final DateTimeFormatter MATCH_DATE_PARSER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:sszzz");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MatchDataXml.class, this::parseEvents)
                .build();
    }

    private void parseEvents(MatchDataXml dataXml) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        try {
            Document document = buildDOMDocument(dataXml.getEvents());
            MatchData result = parseDOM(document);
            log.info("parsed {}", result);

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

    private MatchData parseDOM(Document document) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();

        MatchData result = new MatchData();

        result.setId(xPath.compile("/MatchData/@uID").evaluate(document));
        result.setUpdatedAt(ZonedDateTime.parse(
                xPath.compile("/MatchData/@last_modified").evaluate(document),
                LAST_MODIFIED_PARSER));

        result.setMatchStartTime(ZonedDateTime.parse(
                xPath.compile("/MatchData/MatchInfo/Date").evaluate(document) +
                        xPath.compile("/MatchData/MatchInfo/TZ").evaluate(document),
                MATCH_DATE_PARSER));

        result.setVenue(xPath.compile("/MatchData/Stat[@Type=\"Venue\"]").evaluate(document));
        result.setCity(xPath.compile("/MatchData/Stat[@Type=\"City\"]").evaluate(document));

        result.setHomeTeam(xPath.compile("/MatchData/TeamData[@Side=\"Home\"]/@TeamRef").evaluate(document));
        result.setHomeTeamScore(Integer.parseInt(
                xPath.compile("/MatchData/TeamData[@Side=\"Home\"]/@Score").evaluate(document)));

        result.setAwayTeam(xPath.compile("/MatchData/TeamData[@Side=\"Away\"]/@TeamRef").evaluate(document));
        result.setAwayTeamScore(Integer.parseInt(
                xPath.compile("/MatchData/TeamData[@Side=\"Away\"]/@Score").evaluate(document)));

        result.setWinner(xPath.compile("/MatchData/MatchInfo/@MatchWinner").evaluate(document));

        return result;
    }
}
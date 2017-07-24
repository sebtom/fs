package com.flipsports.xml.opta.f1.matchData;

import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MatchDataTransformer {

    public static final DateTimeFormatter LAST_MODIFIED_PARSER = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxxx");
    public static final DateTimeFormatter MATCH_DATE_PARSER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:sszzz");

    public MatchData parseDOM(Document document) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();

        MatchData result = new MatchData();

        result.setId(xPath.compile("/MatchData/@uID").evaluate(document));
        result.setUpdatedAt(LocalDateTime.parse(
                xPath.compile("/MatchData/@last_modified").evaluate(document),
                LAST_MODIFIED_PARSER));

        result.setMatchStartTime(LocalDateTime.parse(
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

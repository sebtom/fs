package com.flipsports.xml.opta.f1.matchData;

import com.flipsports.test.utils.XmlResource;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchDataTransformerTest implements XmlResource {

    private MatchDataTransformer objectUnderTest;

    @BeforeTest
    public void setUp() {
        objectUnderTest = new MatchDataTransformer();
    }

    @Test
    public void shouldBuildMatchDataWhenValidXMLProvided() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        //given
        Document givenDocument = readXml("com/flipsports/xml/opta/f1/matchData/valid.xml");

        //when
        MatchData actual = objectUnderTest.parseDOM(givenDocument);

        //then
        assertThat(actual.getId()).isEqualTo("g855174");
        assertThat(actual.getUpdatedAt()).isEqualTo(LocalDateTime.of(2016, Month.AUGUST, 14, 19, 1, 43));
        assertThat(actual.getMatchStartTime()).isEqualTo(LocalDateTime.of(2016, Month.AUGUST, 13, 15, 0, 0));
        assertThat(actual.getVenue()).isEqualTo("Turf Moor");
        assertThat(actual.getCity()).isEqualTo("Burnley");
        assertThat(actual.getHomeTeam()).isEqualTo("t90");
        assertThat(actual.getHomeTeamScore()).isEqualTo(0);
        assertThat(actual.getAwayTeam()).isEqualTo("t80");
        assertThat(actual.getAwayTeamScore()).isEqualTo(1);
        assertThat(actual.getWinner()).isEqualTo("t80");
    }
}

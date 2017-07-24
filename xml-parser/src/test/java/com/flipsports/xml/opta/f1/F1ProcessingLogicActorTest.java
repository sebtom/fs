package com.flipsports.xml.opta.f1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.flipsports.test.utils.XmlResource;
import com.flipsports.xml.opta.f1.matchData.MatchDataXml;
import org.powermock.reflect.Whitebox;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class F1ProcessingLogicActorTest implements XmlResource {
    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void shouldGroupMatchDataWhenNestedElements() throws InterruptedException {
        //given
        final TestKit testProbe = new TestKit(system);
        final Props props = Props.create(F1ProcessingLogicActorIsolated.class, () -> new F1ProcessingLogicActorIsolated(testProbe.getTestActor()));
        final ActorRef logic = system.actorOf(props);

        List<XMLEvent> givenEvents = Arrays.asList(
                mockStartElement("MatchData"),
                mockStartElement("someTag1"),
                mockStartElement("someTag2"),
                mockStartElement("someTag3"),
                mockEndElement("someTag3"),
                mockStartElement("someTag4"),
                mockStartElement("someTag5"),
                mockCharacters(),
                mockCharacters(),
                mockCharacters(),
                mockCharacters(),
                mockEndElement("someTag5"),
                mockEndElement("someTag4"),
                mockEndElement("someTag2"),
                mockEndElement("someTag1"),
                mockEndElement("MatchData")
        );

        //when
        givenEvents.forEach(event -> logic.tell(event, ActorRef.noSender()));

        //then
        MatchDataXml matchDataXml = testProbe.expectMsgClass(MatchDataXml.class);
        assertThat(matchDataXml.getEvents()).containsExactlyElementsOf(givenEvents);
    }

    @Test
    public void shouldSplitMatchDataWhenMultipleOccurrences() throws InterruptedException {
        //given
        final TestKit testProbe = new TestKit(system);
        final Props props = Props.create(F1ProcessingLogicActorIsolated.class, () -> new F1ProcessingLogicActorIsolated(testProbe.getTestActor()));
        final ActorRef logic = system.actorOf(props);

        List<XMLEvent> givenEvents = Arrays.asList(
                mockStartElement("MatchData"),
                mockCharacters(),
                mockEndElement("MatchData"),
                mockStartElement("MatchData"),
                mockCharacters(),
                mockCharacters(),
                mockEndElement("MatchData"),
                mockStartElement("MatchData"),
                mockEndElement("MatchData"),
                mockStartElement("MatchData"),
                mockEndElement("MatchData"),
                mockStartElement("MatchData"),
                mockEndElement("MatchData"),
                mockStartElement("MatchData"),
                mockEndElement("MatchData")
        );

        //when
        givenEvents.forEach(event -> logic.tell(event, ActorRef.noSender()));

        //then
        MatchDataXml matchDataXml = testProbe.expectMsgClass(MatchDataXml.class);
        assertThat(matchDataXml.getEvents()).hasSize(3);

        matchDataXml = testProbe.expectMsgClass(MatchDataXml.class);
        assertThat(matchDataXml.getEvents()).hasSize(4);

        matchDataXml = testProbe.expectMsgClass(MatchDataXml.class);
        assertThat(matchDataXml.getEvents()).hasSize(2);

        matchDataXml = testProbe.expectMsgClass(MatchDataXml.class);
        assertThat(matchDataXml.getEvents()).hasSize(2);

        matchDataXml = testProbe.expectMsgClass(MatchDataXml.class);
        assertThat(matchDataXml.getEvents()).hasSize(2);

        matchDataXml = testProbe.expectMsgClass(MatchDataXml.class);
        assertThat(matchDataXml.getEvents()).hasSize(2);
    }

    private StartElement mockStartElement(String name) {
        StartElement result = mock(StartElement.class);
        given(result.getName()).willReturn(new QName(name));
        return result;
    }

    private Characters mockCharacters() {
        return mock(Characters.class);
    }

    private EndElement mockEndElement(String name) {
        EndElement result = mock(EndElement.class);
        given(result.getName()).willReturn(new QName(name));
        return result;
    }

    private class F1ProcessingLogicActorIsolated extends F1ProcessingLogicActor {
        public F1ProcessingLogicActorIsolated(ActorRef testProbe) {
            Whitebox.setInternalState(this, "matchDataProcessor", testProbe);
        }

        @Override
        protected void configureProcessors() {
        }
    }
}
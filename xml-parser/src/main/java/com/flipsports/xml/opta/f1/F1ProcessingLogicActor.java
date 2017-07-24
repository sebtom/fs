package com.flipsports.xml.opta.f1;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.FI;
import akka.routing.FromConfig;
import com.flipsports.xml.ProcessingLogicActor;
import com.flipsports.xml.opta.f1.matchData.MatchDataProcessorActor;
import com.flipsports.xml.opta.f1.matchData.MatchDataXml;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import static com.flipsports.SystemConfig.ACTOR_MATCH_DATA_PROCESSOR;

/*
 * In this variant I decided to massively create concurrent actors for MatchData - not persistent
 * To achieve that the class is collecting all events between <MatchData></MatchData> and send in single message for processing
 * I'm parsing only those elements from this file and I'm creating simple state machine (not full AbstractFSM)
 */
public class F1ProcessingLogicActor extends AbstractActor implements ProcessingLogicActor {
    static public Props props() {
        return Props.create(F1ProcessingLogicActor.class, F1ProcessingLogicActor::new);
    }

    private AbstractActor.Receive inMatchData;
    private AbstractActor.Receive outsideMatchData;

    private ActorRef matchDataProcessor = getContext().actorOf(MatchDataProcessorActor.props().withRouter(FromConfig.getInstance()), ACTOR_MATCH_DATA_PROCESSOR);
    private MatchDataXml matchDataXml = null;

    private FI.TypedPredicate<StartElement> IS_MATCH_DATA_START = element -> "MatchData".equals(element.getName().getLocalPart());
    private FI.TypedPredicate<EndElement> IS_MATCH_DATA_END = element -> "MatchData".equals(element.getName().getLocalPart());

    public F1ProcessingLogicActor() {
        //assuming xml validation, so not checking the right path!!
        outsideMatchData = receiveBuilder()
                .match(StartElement.class, IS_MATCH_DATA_START, this::onMatchDataOpen)
                .match(StartElement.class, this::skipEvent)
                .match(Characters.class, this::skipEvent)
                .match(EndElement.class, this::skipEvent)
                .build();

        inMatchData = receiveBuilder()
                .match(StartElement.class, this::inMatchDataNewEvent)
                .match(Characters.class, this::inMatchDataNewEvent)
                .match(EndElement.class, IS_MATCH_DATA_END, this::inMatchDataEndElement)
                .match(EndElement.class, this::inMatchDataNewEvent)
                .build();
    }

    @Override
    public Receive createReceive() {
        return outsideMatchData;
    }

    private void skipEvent(XMLEvent event) {
    }

    private void onMatchDataOpen(StartElement element) {
        matchDataXml = new MatchDataXml();
        matchDataXml.addEvent(element);
        getContext().become(inMatchData);
    }


    private void inMatchDataNewEvent(XMLEvent event) {
        matchDataXml.addEvent(event);
    }

    private void inMatchDataEndElement(EndElement element) {
        //send data
        matchDataXml.addEvent(element);
        matchDataProcessor.tell(matchDataXml, this.getSelf());
        matchDataXml = null;
        getContext().become(outsideMatchData);
    }
}

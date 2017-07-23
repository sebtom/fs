package com.flipsports.xml.opta.f1;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.flipsports.xml.ProcessingLogicActor;
import com.flipsports.xml.opta.f1.matchData.MatchDataProcessor;
import com.flipsports.xml.opta.f1.matchData.MatchDataXml;

import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import static com.flipsports.SystemConfig.ACTOR_MATCH_DATA_PROCESSOR;

/*
 * In this variant I decided to massively create concurrent actors for MatchData - not persistent
 * To achieve that the class is collecting all events between <MatchData></MatchData> and send in single message for processing
 */
//use AbstractFSM ?
public class F1ProcessingLogic extends ProcessingLogicActor {
    static public Props props() {
        return Props.create(F1ProcessingLogic.class, F1ProcessingLogic::new);
    }

    private ActorRef matchDataProcessor = getContext().actorOf(MatchDataProcessor.props().withRouter(FromConfig.getInstance()), ACTOR_MATCH_DATA_PROCESSOR);

    private MatchDataXml matchDataXml = null;

    private boolean isInMatchData() {
        return matchDataXml != null;
    }

    @Override
    public void initialize() {
    }

    @Override
    protected void onEnterTag(StartElement startElement) {
        if (isInMatchData()) {
            matchDataXml.addEvent(startElement);
        }
        if ("MatchData".equals(getCurrentElement())) {
            //assuming xml validation, so not checking the right format!!
            matchDataXml = new MatchDataXml();
            matchDataXml.setStartElement(startElement);
        }

    }

    @Override
    protected void onCharacters(XmlCharacters characters) {
        if (isInMatchData()) {
            matchDataXml.addEvent(characters.getEvent());
        }
    }

    @Override
    protected void beforeExitTag(EndElement endElement) {
        if ("MatchData".equals(getCurrentElement())) {
            matchDataProcessor.tell(matchDataXml, this.getSelf());
            //send data
            matchDataXml = null;
        }

        if (isInMatchData()) {
            matchDataXml.addEvent(endElement);
        }
    }
}

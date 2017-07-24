package com.flipsports.xml.opta.f9;

import akka.actor.Props;
import com.flipsports.xml.AbstractProcessingLogicActor;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public class F9ProcessingLogic extends AbstractProcessingLogicActor {
    static public Props props() {
        return Props.create(F9ProcessingLogic.class, F9ProcessingLogic::new);
    }

    @Override
    protected void onEnterTag(StartElement startElement) {

    }

    @Override
    protected void onCharacters(Characters characters) {

    }

    @Override
    protected void beforeExitTag(EndElement endElement) {

    }
}

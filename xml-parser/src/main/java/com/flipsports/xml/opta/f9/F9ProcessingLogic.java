package com.flipsports.xml.opta.f9;

import akka.actor.Props;
import com.flipsports.xml.ProcessingLogicActor;

import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public class F9ProcessingLogic extends ProcessingLogicActor {
    static public Props props() {
        return Props.create(F9ProcessingLogic.class, F9ProcessingLogic::new);
    }

    @Override
    public void initialize() {

    }

    @Override
    protected void onEnterTag(StartElement startElement) {

    }

    @Override
    protected void onCharacters(XmlCharacters characters) {

    }

    @Override
    protected void beforeExitTag(EndElement endElement) {

    }
}

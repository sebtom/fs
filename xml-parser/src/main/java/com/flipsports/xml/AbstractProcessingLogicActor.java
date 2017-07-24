package com.flipsports.xml;

import akka.actor.AbstractActor;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import java.util.LinkedList;

public abstract class AbstractProcessingLogicActor extends AbstractActor implements ProcessingLogicActor {

    //potentially some XML names might be repeated in different paths, and I would be interested in context
    protected final LinkedList<String> currentPath = new LinkedList<>();

    protected String getCurrentElement() {
        return currentPath.getLast();
    }


    private void handleStartElement(StartElement elem) {
        currentPath.addLast(elem.getName().getLocalPart());
        onEnterTag(elem);
    }

    protected void onEnterTag(StartElement startElement) {
    }

    protected void onCharacters(Characters characters) {
    }

    public void handleEndElement(EndElement elem) {
        beforeExitTag(elem);
        currentPath.removeLast();
    }

    protected void beforeExitTag(EndElement endElement) {

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartElement.class, this::handleStartElement)
                .match(Characters.class, this::onCharacters)
                .match(EndElement.class, this::handleEndElement)
                .build();
    }
}

package com.flipsports.xml;

import akka.actor.AbstractActor;
import lombok.Value;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import java.util.LinkedList;

public abstract class ProcessingLogicActor extends AbstractActor {

    //potentially some XML names might be repeated in different paths, and I would be interested in context
    protected final LinkedList<String> currentPath = new LinkedList<>();

    public abstract void initialize();

    protected String getCurrentElement() {
        return currentPath.getLast();
    }


    private void handleStartElement(XmlStartTag tag) {
        StartElement elem = tag.getEvent();
        currentPath.addLast(elem.getName().getLocalPart());
        onEnterTag(elem);
    }

    protected abstract void onEnterTag(StartElement startElement);

    protected abstract void onCharacters(XmlCharacters characters);

    public void handleEndElement(XmlEndTag tag) {
        EndElement elem = tag.getEvent();
        beforeExitTag(elem);
        currentPath.removeLast();
    }

    protected abstract void beforeExitTag(EndElement endElement);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(XmlStartTag.class, this::handleStartElement)
                .match(XmlCharacters.class, this::onCharacters)
                .match(XmlEndTag.class, this::handleEndElement)
                .build();
    }

    @Value
    public static class XmlStartTag {
        private StartElement event;
    }

    @Value
    public static class XmlCharacters {
        private Characters event;
    }

    @Value
    public static class XmlEndTag {
        private EndElement event;

    }
}

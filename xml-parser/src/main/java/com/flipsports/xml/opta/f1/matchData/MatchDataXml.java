package com.flipsports.xml.opta.f1.matchData;

import lombok.Data;

import javax.xml.stream.events.XMLEvent;
import java.util.LinkedList;
import java.util.List;

@Data
public class MatchDataXml {
    private List<XMLEvent> events = new LinkedList<>();

    public void addEvent(XMLEvent event) {
        events.add(event);
    }
}

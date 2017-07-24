package com.flipsports.xml.opta.f40;

import akka.actor.Props;
import com.flipsports.utils.XmlSupport;
import com.flipsports.xml.AbstractProcessingLogicActor;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class F40ProcessingLogicActor extends AbstractProcessingLogicActor implements XmlSupport {
    static public Props props() {
        return Props.create(F40ProcessingLogicActor.class, F40ProcessingLogicActor::new);
    }

    public static final DateTimeFormatter FEED_TIME_PARSER = DateTimeFormatter.ofPattern("uuuuMMdd'T'HHmmssxx");

    private LocalDateTime feedTimestamp;

    //what fields could be optional??
    @Override
    protected void onEnterTag(StartElement startElement) {
        switch (getCurrentElement()) {
            case "SoccerFeed":
                feedTimestamp = LocalDateTime.parse(
                        readAttribute(startElement, "timestamp"),
                        FEED_TIME_PARSER);
                break;
            //case ""
            default:
        }
    }

    @Override
    protected void onCharacters(Characters characters) {

    }

    @Override
    protected void beforeExitTag(EndElement endElement) {

    }

//
//
//                if (startElement.getName().getLocalPart().equals("Employee")) {
//        //Get the 'id' attribute from Employee element
//        Attribute idAttr = startElement.getAttributeByName(new QName("id"));
//        if (idAttr != null) {
////                        emp.setId(Integer.parseInt(idAttr.getValue()));
//        }
//    }
//    //set the other varibles from xml elements
//                else if (startElement.getName().getLocalPart().equals("age")) {
//        xmlEvent = xmlEventReader.nextEvent();
////                    emp.setAge(Integer.parseInt(xmlEvent.asCharacters().getData()));
//    } else if (startElement.getName().getLocalPart().equals("filePath")) {
//        xmlEvent = xmlEventReader.nextEvent();
////                    emp.setName(xmlEvent.asCharacters().getData());
//    } else if (startElement.getName().getLocalPart().equals("gender")) {
//        xmlEvent = xmlEventReader.nextEvent();
////                    emp.setGender(xmlEvent.asCharacters().getData());
//    } else if (startElement.getName().getLocalPart().equals("role")) {
//        xmlEvent = xmlEventReader.nextEvent();
////                    emp.setRole(xmlEvent.asCharacters().getData());
//    }

}

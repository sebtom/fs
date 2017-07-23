package com.flipsports.xml.opta.f40;

import akka.actor.Props;
import com.flipsports.utils.DateUtils;
import com.flipsports.utils.XmlSupport;
import com.flipsports.xml.ProcessingLogicActor;

import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import java.time.LocalDateTime;

public class F40ProcessingLogic extends ProcessingLogicActor implements XmlSupport {
    static public Props props() {
        return Props.create(F40ProcessingLogic.class, F40ProcessingLogic::new);
    }

    private LocalDateTime feedTimestamp;

    @Override
    public void initialize() {

    }

    //what fields could be optional??
    @Override
    protected void onEnterTag(StartElement startElement) {
        switch (getCurrentElement()) {
            case "SoccerFeed":
                feedTimestamp = DateUtils.parseIsoOffsetDateTime(
                        readAttribute(startElement, "timestamp"));
                break;
            //case ""
            default:
        }
    }

    @Override
    protected void onCharacters(XmlCharacters characters) {

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

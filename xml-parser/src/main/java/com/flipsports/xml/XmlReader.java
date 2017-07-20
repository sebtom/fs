package com.flipsports.xml;

import akka.actor.AbstractActor;
import akka.actor.Props;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Slf4j
public class XmlReader extends AbstractActor {
    static public Props props() {
        return Props.create(XmlReader.class, XmlReader::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(XmlFilename.class, this::readXmlFile)
                .build();
    }

    private void readXmlFile(XmlFilename file) {
        log.debug("Reading file {}", file.name);

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {

            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(file.name));
            parseXml(xmlEventReader);
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void parseXml(XMLEventReader xmlEventReader) throws XMLStreamException {
        while (xmlEventReader.hasNext()) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();
            if (xmlEvent.isStartElement()) {
                StartElement startElement = xmlEvent.asStartElement();
                log.debug("start elem: {}", startElement.getName());
                if (startElement.getName().getLocalPart().equals("Employee")) {
                    //Get the 'id' attribute from Employee element
                    Attribute idAttr = startElement.getAttributeByName(new QName("id"));
                    if (idAttr != null) {
//                        emp.setId(Integer.parseInt(idAttr.getValue()));
                    }
                }
                //set the other varibles from xml elements
                else if (startElement.getName().getLocalPart().equals("age")) {
                    xmlEvent = xmlEventReader.nextEvent();
//                    emp.setAge(Integer.parseInt(xmlEvent.asCharacters().getData()));
                } else if (startElement.getName().getLocalPart().equals("name")) {
                    xmlEvent = xmlEventReader.nextEvent();
//                    emp.setName(xmlEvent.asCharacters().getData());
                } else if (startElement.getName().getLocalPart().equals("gender")) {
                    xmlEvent = xmlEventReader.nextEvent();
//                    emp.setGender(xmlEvent.asCharacters().getData());
                } else if (startElement.getName().getLocalPart().equals("role")) {
                    xmlEvent = xmlEventReader.nextEvent();
//                    emp.setRole(xmlEvent.asCharacters().getData());
                }
            }
            //if Employee end element is reached, add employee object to list
            if (xmlEvent.isEndElement()) {
                EndElement endElement = xmlEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("Employee")) {
//                    empList.add(emp);
                }
            }
        }
    }

//
//    /**
//     * StAX processor
//     * @param xml
//     */
//    def parseXml(xml: XMLEventReader) {
//        var inPage = false
//        var inPageText = false
//        var inPageTitle = false
//        var skipPage = false
//        var pageText = ""
//        var lastTitle = ""
//
//        while (xml.hasNext) {
//            log.debug("next: " + xml)
//            val t1 = TimeLib.getTime
//
//            xml.next match {
//                //          case EvElemStart(pre, label, attrs, scope) if label == "mediawiki" =>
//                //            println(s"!!! - $label")
//                case EvElemStart(_, "page", _, _)               => inPage = true
//                case EvElemStart(_, "title", _, _) if inPage    => inPageTitle = true
//                case EvElemStart(_, "redirect", _, _) if inPage => skipPage = true // just skip them now
//
//                case EvElemStart(_, label, _, _) if inPage && label == "text" => inPageText = true
//                case EvElemStart(_, label, _, _) if inPage => log.debug(s"Elem of page: <$label>")
//                case EvElemStart(pre, label, attrs, scope) => log.debug("START: ", pre, label, attrs, scope)
//
//                case EvElemEnd(_, "page") =>
//                    inPage = false
//                    skipPage = false
//                    lastTitle = "" // safer to clear
//
//                case EvElemEnd(_, "title")  => inPageTitle = false
//                case EvElemEnd(_, "text")   =>
//                    log.debug(s"Got full article text [$lastTitle] - process it!")
//
//                    context.actorSelection("/user/article") ! Article(lastTitle, pageText)
//
//                    pageText = ""
//                    inPageText = false
//
//                case EvElemEnd(pre, label)                    => log.debug("END: ",pre, label)
//                case EvText(text) if inPageTitle              => lastTitle = text
//                case EvText(text) if inPageText && !skipPage  => pageText += text
//                case EvText(text)                             => log.debug("TEXT: " + text)
//
//                case EvEntityRef(entity) if inPageText =>
//                    // TODO: add pageText entities to text!!! (how about entities from titles?)
//                    log.debug(s"Entity in text: ${entity}")
//                case EvEntityRef(entity) => log.debug("ENTITY: " + entity)
//                    //        case POISON =>
//                case EvProcInstr(target, text) => log.debug(s"PROCINSTR: $target, $text")
//                case EvComment(text) => log.debug(s"EVCOMMENT: $text")
//                case _ =>
//            }
//            Parser.met ! ExecTime("xmlHasNext", TimeLib.getTime - t1)
//        }
//    }
//}


    @AllArgsConstructor
    static public class XmlFilename {
        public final String name;
    }
}


//class XmlReader extends Actor {
//    val log = Logging(context.system, this)
//
//    override def receive: Receive = {
//        case XmlFilename(name) =>
//            readXmlFile(name)
//            println(s"Finished: $name")
//
//            context.actorSelection("/user/longestArticle") ! "stats"
//
//            // get latest numbers, they may not be ready yet
//            for (i <- 1.to(5)) {
//            Parser.met ! ShowTime()
//            Thread.sleep(10 * 1000)
//        }
//
//        case _ =>
//    }
//
//    def readXmlFile(name: String) = {
//        log.debug(s"Reading file $name")
//
//        // parse xml content
//
//        val t1 = TimeLib.getTime
//
//        val xml = new XMLEventReader(Source.fromFile(name))
//
//        val t2 = TimeLib.getTime
//        Parser.met ! ExecTime("readXml-FromFile", t2-t1)
//
//        parseXml(xml)
//
//        val t3 = TimeLib.getTime
//        Parser.met ! ExecTime("readXml-parseXml", t3-t2)
//        println(f"Exec time: ${(t3 - t1) / Math.pow(10, 9)}%.2f sec")
//
//    }
//
//    /**
//     * StAX processor
//     * @param xml
//     */
//    def parseXml(xml: XMLEventReader) {
//        var inPage = false
//        var inPageText = false
//        var inPageTitle = false
//        var skipPage = false
//        var pageText = ""
//        var lastTitle = ""
//
//        while (xml.hasNext) {
//            log.debug("next: " + xml)
//            val t1 = TimeLib.getTime
//
//            xml.next match {
//                //          case EvElemStart(pre, label, attrs, scope) if label == "mediawiki" =>
//                //            println(s"!!! - $label")
//                case EvElemStart(_, "page", _, _)               => inPage = true
//                case EvElemStart(_, "title", _, _) if inPage    => inPageTitle = true
//                case EvElemStart(_, "redirect", _, _) if inPage => skipPage = true // just skip them now
//
//                case EvElemStart(_, label, _, _) if inPage && label == "text" => inPageText = true
//                case EvElemStart(_, label, _, _) if inPage => log.debug(s"Elem of page: <$label>")
//                case EvElemStart(pre, label, attrs, scope) => log.debug("START: ", pre, label, attrs, scope)
//
//                case EvElemEnd(_, "page") =>
//                    inPage = false
//                    skipPage = false
//                    lastTitle = "" // safer to clear
//
//                case EvElemEnd(_, "title")  => inPageTitle = false
//                case EvElemEnd(_, "text")   =>
//                    log.debug(s"Got full article text [$lastTitle] - process it!")
//
//                    context.actorSelection("/user/article") ! Article(lastTitle, pageText)
//
//                    pageText = ""
//                    inPageText = false
//
//                case EvElemEnd(pre, label)                    => log.debug("END: ",pre, label)
//                case EvText(text) if inPageTitle              => lastTitle = text
//                case EvText(text) if inPageText && !skipPage  => pageText += text
//                case EvText(text)                             => log.debug("TEXT: " + text)
//
//                case EvEntityRef(entity) if inPageText =>
//                    // TODO: add pageText entities to text!!! (how about entities from titles?)
//                    log.debug(s"Entity in text: ${entity}")
//                case EvEntityRef(entity) => log.debug("ENTITY: " + entity)
//                    //        case POISON =>
//                case EvProcInstr(target, text) => log.debug(s"PROCINSTR: $target, $text")
//                case EvComment(text) => log.debug(s"EVCOMMENT: $text")
//                case _ =>
//            }
//            Parser.met ! ExecTime("xmlHasNext", TimeLib.getTime - t1)
//        }
//    }
//}

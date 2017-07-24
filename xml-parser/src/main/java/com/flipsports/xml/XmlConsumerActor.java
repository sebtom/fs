package com.flipsports.xml;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.FI;
import akka.routing.FromConfig;
import com.flipsports.filesystem.FileMonitorMsg.FileCreated;
import com.flipsports.utils.FileSupport;
import com.flipsports.xml.XmlReaderActor.XmlData;
import lombok.extern.slf4j.Slf4j;

import static com.flipsports.SystemConfig.ACTOR_XML_PARSER;

@Slf4j
public class XmlConsumerActor extends AbstractActor implements FileSupport {
    static public Props props() {
        return Props.create(XmlConsumerActor.class, XmlConsumerActor::new);
    }

    private ActorRef xmlParser = getContext().actorOf(XmlReaderActor.props().withRouter(FromConfig.getInstance()), ACTOR_XML_PARSER);

    private final FI.TypedPredicate<FileCreated> IS_XML = fileCreated -> hasExtension(fileCreated.getFile(), "xml");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FileCreated.class, IS_XML, this::processFile)
                .match(FileCreated.class, this::skipFile)
                .build();
    }

    private void skipFile(FileCreated file) {
        log.warn("Not supported file: {}", file.getFile());
    }

    private void processFile(FileCreated file) {
        xmlParser.tell(new XmlData(file.getFile()), this.getSelf());
    }
}

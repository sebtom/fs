package com.flipsports.xml.opta.f40;

import akka.actor.Props;
import com.flipsports.utils.XmlSupport;
import com.flipsports.xml.AbstractProcessingLogicActor;

public class F40ProcessingLogicActor extends AbstractProcessingLogicActor implements XmlSupport {
    public static Props props() {
        return Props.create(F40ProcessingLogicActor.class, F40ProcessingLogicActor::new);
    }
}

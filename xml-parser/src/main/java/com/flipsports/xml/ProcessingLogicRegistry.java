package com.flipsports.xml;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.flipsports.xml.opta.f1.F1ProcessingLogic;
import com.flipsports.xml.opta.f40.F40ProcessingLogic;
import com.flipsports.xml.opta.f9.F9ProcessingLogic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ProcessingLogicRegistry {

    private static final Map<String, Class<? extends ProcessingLogicActor>> MAPPING;

    static {
        Map<String, Class<? extends ProcessingLogicActor>> result = new HashMap<>();
        result.put("f1.xml", F1ProcessingLogic.class);
        result.put("f1a.xml", F1ProcessingLogic.class);
        result.put("f9.xml", F9ProcessingLogic.class);
        result.put("f40.xml", F40ProcessingLogic.class);
        MAPPING = result;
    }

    public static ActorRef lookup(String filename, AbstractActor.ActorContext context) {
        if (!MAPPING.containsKey(filename)) {
            throw new IllegalStateException(String.format("Unsupported file: %s", filename));
        }
        Class<? extends ProcessingLogicActor> processingClass = MAPPING.get(filename);
        try {
            Method propsMethod = processingClass.getMethod("props");
            Props props = (Props) propsMethod.invoke(null);
            return context.actorOf(props);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(String.format("Could not prepare ProcessingLogicActor for file: %s", filename), e);
        }
    }
}

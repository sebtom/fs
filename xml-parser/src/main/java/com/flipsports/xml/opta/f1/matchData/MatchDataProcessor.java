package com.flipsports.xml.opta.f1.matchData;

import akka.actor.AbstractActor;
import akka.actor.Props;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchDataProcessor extends AbstractActor {
    public static Props props() {
        return Props.create(MatchDataProcessor.class, MatchDataProcessor::new);
    }

    private int number;
    private static int counter = 0;


    public MatchDataProcessor() {
        number = getNumber();
        log.warn("initializing MatchDataProcessor{}", number);
    }


    public int getNumber() {
        synchronized (MatchDataProcessor.class) {
            return counter++;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MatchDataXml.class, this::parseEvents)
                .build();
    }

    private void parseEvents(MatchDataXml dataXml) throws InterruptedException {
        test(dataXml);
//        dataXml.getStartElement()
    }

    private void test(MatchDataXml dataXml) throws InterruptedException {
        Thread.sleep(100L);
        log.info("MatchDataProcessor{} parsing: {}", number, dataXml.getStartElement().toString());
    }

}

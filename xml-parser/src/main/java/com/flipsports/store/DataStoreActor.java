package com.flipsports.store;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.flipsports.xml.opta.f1.matchData.MatchData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DataStoreActor extends AbstractActor {
    public static Props props(DataStore store) {
        return Props.create(DataStoreActor.class, () -> new DataStoreActor(store));
    }

    private final DataStore store;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MatchData.class, store::persist)
                .build();
    }
}

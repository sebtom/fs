package com.flipsports.store;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleLoggingStore implements DataStore {
    @Override
    public void persist(Object object) {
        log.info("New Entity to persist: {}", object);
    }
}

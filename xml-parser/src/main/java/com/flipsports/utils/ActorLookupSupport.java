package com.flipsports.utils;

public interface ActorLookupSupport {

    default String top(String path) {
        return "/user/" + path;
    }
}

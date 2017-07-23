package com.flipsports.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateUtils {

    public static final DateTimeFormatter ISO_OFFSET_DATE_TIME = DateTimeFormatter.ofPattern("uuuuMMdd'T'HHmmssxx");

    public static LocalDateTime parseIsoOffsetDateTime(String toParse) {
        return LocalDateTime.parse(toParse, ISO_OFFSET_DATE_TIME);
    }
}

package com.flipsports.filesystem;

import lombok.Value;

import java.io.File;

public class FileMonitorMsg {
    @Value
    public static class FileCreated {
        private File file;
    }
}

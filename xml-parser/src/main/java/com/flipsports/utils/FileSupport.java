package com.flipsports.utils;

import java.io.File;

public interface FileSupport {

    default boolean hasExtension(File file, String extension) {
        return file.getName().endsWith("." + extension);
    }
}

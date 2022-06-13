package com.ossprj.fileset.task.process.core;

import lombok.Data;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class FilesetContext {

    private final Path path;
    private final Map<String, Object> context;

    public FilesetContext(final Path path) {
        this.path = path;
        this.context = new LinkedHashMap<>();
    }
}

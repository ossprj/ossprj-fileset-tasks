package com.ossprj.fileset.task.process.core;

public interface FilesetProcessor {

    String getName();

    void init();

    void process(FilesetContext filesetContext);

    void close();
}

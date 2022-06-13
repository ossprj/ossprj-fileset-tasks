package com.ossprj.fileset.task.process.processor.report;

import lombok.Data;

import java.nio.file.Path;

@Data
public class BuildReportConfiguration {

    private String header;
    private String template;
    private Path outputFilePath;

}

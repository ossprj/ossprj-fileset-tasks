package com.ossprj.fileset.task.process.processor.identifier;

import lombok.Data;

import java.util.regex.Pattern;

@Data
public class ExtractIdentifiersConfiguration {

    private Pattern regex;
    private Integer collectionGroup;
    private String collectionValue;
    private Integer volumeGroup;
    private String volumeValue;
    private Integer partitionGroup;
    private String partitionValue;

}

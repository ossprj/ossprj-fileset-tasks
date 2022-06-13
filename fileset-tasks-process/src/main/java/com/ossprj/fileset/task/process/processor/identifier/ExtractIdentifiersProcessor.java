package com.ossprj.fileset.task.process.processor.identifier;


import com.ossprj.fileset.task.process.core.AbstractFilesetProcessor;
import com.ossprj.fileset.task.process.core.FilesetContext;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.regex.Matcher;

public class ExtractIdentifiersProcessor extends AbstractFilesetProcessor<ExtractIdentifiersConfiguration> {

    public ExtractIdentifiersProcessor(final Map<String, Object> configMap) {
        super(MethodHandles.lookup().lookupClass().getName(), configMap);
    }

    @Override
    public void process(final FilesetContext filesetContext) {
        final Matcher matcher = configuration.getRegex().matcher(filesetContext.getPath().toFile().getAbsolutePath());

        if (!matcher.find()) {
            throw new RuntimeException("regex doesn't match");
        }

        final String collection = configuration.getCollectionValue() != null ?
                configuration.getCollectionValue() : matcher.group(configuration.getCollectionGroup());
        if (collection != null && !collection.isEmpty()) {
            filesetContext.getContext().put("FilesetCollection", collection);
        }

        final String volume = configuration.getVolumeValue() != null ?
                configuration.getVolumeValue() : matcher.group(configuration.getVolumeGroup());
        if (volume != null && !volume.isEmpty()) {
            filesetContext.getContext().put("FilesetVolume", volume);
        }

        final String partition = configuration.getPartitionValue() != null ?
                configuration.getPartitionValue() : matcher.group(configuration.getPartitionGroup());
        if (partition != null && !partition.isEmpty()) {
            filesetContext.getContext().put("FilesetPartition", partition);
        }
    }

}

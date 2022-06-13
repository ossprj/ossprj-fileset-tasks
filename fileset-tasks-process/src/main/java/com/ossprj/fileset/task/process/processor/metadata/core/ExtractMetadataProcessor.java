package com.ossprj.fileset.task.process.processor.metadata.core;

import com.ossprj.commons.file.function.CalculateDirectoryContentHash;
import com.ossprj.fileset.task.process.core.AbstractFilesetProcessor;
import com.ossprj.fileset.task.process.core.FilesetContext;
import com.ossprj.fileset.task.process.function.GetFilesetSize;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public class ExtractMetadataProcessor extends AbstractFilesetProcessor<ExtractMetadataConfiguration> {

    private final GetFilesetSize getFilesetSize = new GetFilesetSize();
    private final CalculateDirectoryContentHash calculateDirectoryContentHash = new CalculateDirectoryContentHash();

    public ExtractMetadataProcessor(Map<String, Object> configMap) {
        super(MethodHandles.lookup().lookupClass().getName(), configMap);
    }

    @Override
    public void process(final FilesetContext filesetContext) {

        filesetContext.getContext().put("FilesetName", filesetContext.getPath().toFile().getName());
        filesetContext.getContext().put("FilesetPath", filesetContext.getPath().toAbsolutePath());

        final Long filesetSize = getFilesetSize.apply(filesetContext.getPath());
        filesetContext.getContext().put("FilesetSize", filesetSize);

        final String directoryHash;
        if (filesetContext.getPath().toFile().isDirectory() && configuration.isComputeDirectoryHash()) {
            directoryHash = calculateDirectoryContentHash.apply(filesetContext.getPath());
            filesetContext.getContext().put("DirectoryHash", directoryHash);
        }

    }
}

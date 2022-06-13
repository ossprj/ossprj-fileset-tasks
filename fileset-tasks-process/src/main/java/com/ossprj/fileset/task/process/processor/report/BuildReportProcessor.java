package com.ossprj.fileset.task.process.processor.report;

import com.ossprj.commons.text.function.ReplaceTokens;
import com.ossprj.fileset.task.process.core.AbstractFilesetProcessor;
import com.ossprj.fileset.task.process.core.FilesetContext;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.Map;

public class BuildReportProcessor extends AbstractFilesetProcessor<BuildReportConfiguration> {

    private final ReplaceTokens replaceTokens = new ReplaceTokens();

    FileWriter fileWriter;

    public BuildReportProcessor(Map<String, Object> configMap) {
        super(MethodHandles.lookup().lookupClass().getName(), configMap);
    }

    @Override
    public void process(final FilesetContext filesetContext) {
        try {
            fileWriter.write(encode(replaceTokens.apply(configuration.getTemplate(), filesetContext.getContext())) + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Encode double quotes and commas since we are generating a CSV file
    // Turn nulls into empty strings
    private String encode(final Object input) {
        if (input == null) {
            return "";
        }

        return "\"" + input.toString()
                .replace("\"", "\\\"")
                //.replace(",", "\\,")
                + "\"";
    }

    @Override
    protected void doInit() {
        final Path outputFilePathParent = configuration.getOutputFilePath().toAbsolutePath().getParent();
        if (!outputFilePathParent.toFile().exists()) {
            throw new IllegalArgumentException("the parent of the outputFilePath must exist");
        }
        if (!outputFilePathParent.toFile().canWrite()) {
            throw new IllegalArgumentException("the parent of the outputFilePath must be writeable");
        }

        try {
            fileWriter = new FileWriter(configuration.getOutputFilePath().toFile());
            fileWriter.write(configuration.getHeader());
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() {
        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

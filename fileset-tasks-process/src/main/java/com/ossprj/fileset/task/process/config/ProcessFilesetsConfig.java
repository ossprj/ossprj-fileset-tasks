package com.ossprj.fileset.task.process.config;

import com.ossprj.commons.file.model.SearchPath;
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
@ConfigurationProperties("process-filesets")
@Data
//@Validated
public class ProcessFilesetsConfig {

    @NotNull
    private List<SearchPath> sourceSearchPaths;

    private List<FilesetProcessorConfig> filesetProcessors;

}

package com.ossprj.fileset.task;

import com.ossprj.commons.file.model.SearchPath;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.List;

@Component
@ConfigurationProperties("partition-filesets")
@Data
@Validated
public class PartitionFilesetsConfig {

    @NotNull
    private List<SearchPath> sourceSearchPaths;

    @NotNull
    private Path targetBasePath;

    @NotNull
    private Boolean createTargetBasePathIfMissing = Boolean.FALSE;

    @NotNull
    private Long targetPartitionSize;

    @NotNull
    private Boolean dryRun = Boolean.FALSE;
}

package com.ossprj.fileset.task.process.config;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Validated
public class FilesetProcessorConfig {

    @NotNull
    private String className;

    private Map<String, Object> config;

}

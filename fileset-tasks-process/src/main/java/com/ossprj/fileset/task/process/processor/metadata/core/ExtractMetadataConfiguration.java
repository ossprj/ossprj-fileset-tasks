package com.ossprj.fileset.task.process.processor.metadata.core;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExtractMetadataConfiguration {

    @NotNull
    private boolean computeFilesetSize = Boolean.TRUE;

    @NotNull
    private boolean computeDirectoryHash = Boolean.FALSE;

    /*@NotNull
    private boolean computeDirectoryHashIgnoringEmptyFiles = Boolean.FALSE;*/


}

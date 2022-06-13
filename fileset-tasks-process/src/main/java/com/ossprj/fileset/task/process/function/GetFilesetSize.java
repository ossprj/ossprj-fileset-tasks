package com.ossprj.fileset.task.process.function;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public class GetFilesetSize implements Function<Path, Long> {

    @Override
    public Long apply(final Path path) {
        final long fileSize;
        // If this is a file, then just set to the size to the file length
        if (path.toFile().isFile()) {
            fileSize = path.toFile().length();
        } else {
            // otherwise, its a directory, so compute the size of all the files in it.
            // Get the current size of the target base Path
            try {
                long currentSize = Files.walk(path)
                        .filter(p -> p.toFile().isFile())
                        .mapToLong(p -> p.toFile().length())
                        .sum();
                fileSize = currentSize;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return fileSize;
    }
}

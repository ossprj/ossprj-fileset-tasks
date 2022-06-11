package com.ossprj.fileset.task;

import com.ossprj.commons.file.function.GetPathsFromSearchPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class PartitionFilesetsTask {

    final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    final GetPathsFromSearchPaths getPathsFromSearchPaths = new GetPathsFromSearchPaths();

    @Bean
    public CommandLineRunner commandLineRunner(final PartitionFilesetsConfig config) {

        return (strings) -> {

            logger.info("Start PartitionFilesets Task");
            logger.info("config: " + config);

            final List<Path> sourcePaths = getPathsFromSearchPaths.apply(config.getSourceSearchPaths());

            if (logger.isDebugEnabled()) {
                logger.debug("sourcePaths: " + sourcePaths);
            }
            logger.info("targetBasePath: " + config.getTargetBasePath());

            // If target base path doesn't exist should we create it ?
            if (!config.getTargetBasePath().toFile().exists() && config.getCreateTargetBasePathIfMissing()) {
                logger.info("Created (" + config.getTargetBasePath().toFile().mkdirs() + ")" + config.getTargetBasePath());
            }

            if (!config.getTargetBasePath().toFile().exists()) {
                throw new IllegalArgumentException("targetBasePath must exist");
            }

            // Get the current size of the target base Path
            long currentSize = Files.walk(config.getTargetBasePath())
                    .filter(p -> p.toFile().isFile())
                    .mapToLong(p -> p.toFile().length())
                    .sum();

            logger.info("targetBasePath.size: " + currentSize);

            for (final Path sourcePath : sourcePaths) {

                // Get size of current candidate Path
                final long size = Files.walk(sourcePath)
                        .filter(p -> p.toFile().isFile())
                        .mapToLong(p -> p.toFile().length())
                        .sum();

                // See if we can move it into the target base Path without going over the size limit
                final boolean canMove = currentSize + size <= config.getTargetPartitionSize();
                if (logger.isDebugEnabled()) {
                    logger.debug(canMove + " - " + sourcePath + ": " + size);
                }

                // if so move it and add the candidate Path's size to the running total
                if (canMove) {
                    final String sourcePathName = sourcePath.toFile().getName();
                    final Path finalPath = config.getTargetBasePath().resolve(sourcePathName);
                    logger.info((config.getDryRun() ? "DryRun: " : "Moving: ") + sourcePath + " -> " + finalPath);
                    if (!config.getDryRun()) {
                        Files.move(sourcePath, finalPath);
                    }
                    currentSize += size;
                }
            }

            logger.info("End PartitionFilesets Task");
        };
    }
}

package com.ossprj.fileset.task.process;

import com.ossprj.commons.file.function.GetPathsFromSearchPaths;
import com.ossprj.fileset.task.process.config.ProcessFilesetsConfig;
import com.ossprj.fileset.task.process.core.FilesetContext;
import com.ossprj.fileset.task.process.core.FilesetProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProcessFilesetsTask {

    final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    final GetPathsFromSearchPaths getPathsFromSearchPaths = new GetPathsFromSearchPaths();

    @Bean
    public CommandLineRunner commandLineRunner(final ProcessFilesetsConfig processFilesetsConfig) {

        return (strings) -> {

            logger.info("config: " + processFilesetsConfig);

            final List<Path> sourcePaths = getPathsFromSearchPaths.apply(processFilesetsConfig.getSourceSearchPaths());
            logger.debug("sourcePaths: " + sourcePaths);

            logger.info("Loading FilesetProcessors");
            final List<FilesetProcessor> filesetProcessors = processFilesetsConfig.getFilesetProcessors().stream().map(filesetProcessorConfig -> {
                try {
                    return (FilesetProcessor) Class.forName(filesetProcessorConfig.getClassName())
                            .getConstructor(Map.class)
                            .newInstance(filesetProcessorConfig.getConfig());
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            logger.debug("filesetProcessors: " + filesetProcessors);

            logger.info("Initializing FilesetProcessors");
            filesetProcessors.forEach(FilesetProcessor::init);

            //final ProcessorContext processorContext = new ProcessorContext();

            logger.info("Processing Filesets");
            for (final Path sourcePath : sourcePaths) {
                logger.debug("Processing sourcePath: " + sourcePath);

                final FilesetContext filesetContext = new FilesetContext(sourcePath);

                // apply all the FilesetProcessors in order, sharing the filesetContext between them
                for (final FilesetProcessor filesetProcessor : filesetProcessors) {
                    logger.debug("FilesetProcessor: " + filesetProcessor.getName());
                    filesetProcessor.process(filesetContext);
                    logger.debug("filesetContext: " + filesetContext);
                }
            }

            logger.info("Closing FilesetProcessors");
            filesetProcessors.forEach(FilesetProcessor::close);

        };
    }
}

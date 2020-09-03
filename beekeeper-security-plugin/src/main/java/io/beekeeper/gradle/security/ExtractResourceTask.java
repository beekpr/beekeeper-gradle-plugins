package io.beekeeper.gradle.security;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ExtractResourceTask extends DefaultTask implements Task {

    @Input
    private String resourcePath;

    @OutputFile
    private String destination;

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @TaskAction
    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
    public void extract() throws IOException {

        try (final InputStream stream = ExtractResourceTask.class.getClassLoader().getResourceAsStream(resourcePath)) {
            getLogger().info("Extracting resource...");
            File destination = getProject().file(getDestination());

            IGNORE_RESULT(destination.getParentFile().mkdirs());
            Files.copy(stream, Paths.get(destination.toURI()), StandardCopyOption.REPLACE_EXISTING);
            getLogger().info("Resource successfully copied.");
        }
    }

    @SuppressWarnings("unused")
    private static void IGNORE_RESULT(boolean b) {
    }
}

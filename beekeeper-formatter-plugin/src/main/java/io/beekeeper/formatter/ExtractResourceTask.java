package io.beekeeper.formatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import com.diffplug.common.io.ByteSource;
import com.diffplug.common.io.Resources;

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
    public void extract() throws IOException {
        getLogger().info("Extracting resource...");

        ByteSource source = Resources.asByteSource(Resources.getResource(resourcePath));
        File destination = getProject().file(getDestination());

        IGNORE_RESULT(destination.getParentFile().mkdirs());
        Files.copy(source.openStream(), Paths.get(destination.toURI()), StandardCopyOption.REPLACE_EXISTING);
        getLogger().info("Resource successfully copied.");
    }

    @SuppressWarnings("unused")
    private static void IGNORE_RESULT(boolean b) {
    }
}

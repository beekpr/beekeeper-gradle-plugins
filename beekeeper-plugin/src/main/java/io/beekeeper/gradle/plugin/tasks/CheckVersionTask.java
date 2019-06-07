package io.beekeeper.gradle.plugin.tasks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Task;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import com.github.zafarkhaja.semver.Version;

import io.beekeeper.gradle.plugin.BeekeeperPlugin;
import lombok.Getter;
import lombok.Setter;

public class CheckVersionTask extends DefaultTask implements Task {

    @Input
    @Getter
    @Setter
    private String url;

    @TaskAction
    public void check() throws MalformedURLException, IOException {
        try (InputStream stream = new URL(this.url).openStream()) {
            String requiredVersionString = read(stream);
            String actualVersionString = getCurrentVersion();

            Version required = Version.valueOf(requiredVersionString);
            Version actual = Version.valueOf(actualVersionString);

            if (required.greaterThan(actual)) {
                throw new GradleException(
                        String.format(
                            "Plugin requires upgrade.\n\t Required version: '%s'.\n\t Current version: '%s'",
                            requiredVersionString,
                            actualVersionString
                        )
                );
            }
        }
    }

    private String read(InputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

    private String getCurrentVersion() throws IOException {
        Properties properties = new Properties();
        properties.load(BeekeeperPlugin.class.getResourceAsStream("/plugin.properties"));
        return properties.getProperty("version");
    }
}
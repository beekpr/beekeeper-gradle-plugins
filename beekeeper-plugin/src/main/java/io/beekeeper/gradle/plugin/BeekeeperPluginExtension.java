package io.beekeeper.gradle.plugin;

import lombok.Data;

@Data
public class BeekeeperPluginExtension {

    private static final String DEFAULT_REQUIRED_VERSION_URL =
        "https://raw.githubusercontent.com/beekpr/beekeeper-gradle-plugins/master/REQUIRED_VERSION";

    private String requiredVersionUrl = DEFAULT_REQUIRED_VERSION_URL;
}

package io.beekeeper.gradle.security;

import org.cyclonedx.gradle.CycloneDxPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class BeekeeperCycloneDxPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.security.cyclonedx";

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(CycloneDxPlugin.class);
    }

}

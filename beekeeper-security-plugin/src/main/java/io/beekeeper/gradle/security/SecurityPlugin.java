package io.beekeeper.gradle.security;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class SecurityPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.security";

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(BeekeeperDependencyCheckPlugin.class);
        project.getPluginManager().apply(PatchVulnerableLibrariesPlugin.class);
        project.getPluginManager().apply(BeekeeperCycloneDxPlugin.class);
    }

}

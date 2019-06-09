package io.beekeeper.gradle.security;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin;

public class SecurityPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.security";

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(DependencyCheckPlugin.class);
    }

}

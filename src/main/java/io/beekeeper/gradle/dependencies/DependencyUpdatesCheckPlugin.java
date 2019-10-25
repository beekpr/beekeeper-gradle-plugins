package io.beekeeper.gradle.dependencies;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import com.github.benmanes.gradle.versions.VersionsPlugin;

public class DependencyUpdatesCheckPlugin implements Plugin<Project> {

    public static final String IDENTIFIER = "io.beekeeper.gradle.plugins.dependency-updates";

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(VersionsPlugin.class);
    }
}

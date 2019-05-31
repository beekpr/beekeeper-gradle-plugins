package io.beekeeper.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin;

import io.beekeeper.formatter.FormatterPlugin;

public class BeekeeperPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(FormatterPlugin.class);
        project.getPluginManager().apply(DependencyCheckPlugin.class);

        for (Project subproject : project.getSubprojects()) {
            apply(subproject);
        }
    }
}

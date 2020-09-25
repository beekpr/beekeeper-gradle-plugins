package io.beekeeper.gradle.plugin;

import io.beekeeper.gradle.dependencies.DependencyUpdatesCheckPlugin;
import io.beekeeper.gradle.quarkus.QuarkusDependencies;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import io.beekeeper.gradle.quarkus.QuarkusPlugin;

import io.beekeeper.formatter.FormatterPlugin;
import io.beekeeper.gradle.code.CodeAnalysisPlugin;
import io.beekeeper.gradle.licenses.LicenseCheckPlugin;
import io.beekeeper.gradle.plugin.tasks.CheckVersionTask;
import io.beekeeper.gradle.security.SecurityPlugin;
import io.beekeeper.gradle.testing.TestingPlugin;
import io.beekeeper.ide.IdePlugin;

public class BeekeeperPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugin";

    private BeekeeperPluginExtension pluginExtension;

    @Override
    public void apply(Project project) {
        applyExtensions(project);
        applyTasks(project);
        applyPlugins(project);
    }

    private void applyExtensions(Project project) {
        pluginExtension = project.getExtensions().create("beekeeper", BeekeeperPluginExtension.class);
        project.getExtensions().getExtraProperties().set("beekeeperQuarkus", QuarkusDependencies.getDependencies());
    }

    private void applyTasks(Project project) {
        CheckVersionTask task = project.getTasks().create("beekeeperPluginCheck", CheckVersionTask.class);
        task.setGroup("Verification");
        task.setDescription("Checks that the Beekeeper plugin is at the minimum required version");

        project.afterEvaluate((p) -> {
            task.setUrl(pluginExtension.getRequiredVersionUrl());
        });
    }

    private void applyPlugins(Project project) {
        project.getPluginManager().apply(CodeAnalysisPlugin.class);
        project.getPluginManager().apply(FormatterPlugin.class);
        project.getPluginManager().apply(IdePlugin.class);
        project.getPluginManager().apply(LicenseCheckPlugin.class);
        project.getPluginManager().apply(QuarkusPlugin.class);
        project.getPluginManager().apply(SecurityPlugin.class);
        project.getPluginManager().apply(TestingPlugin.class);
        project.getPluginManager().apply(DependencyUpdatesCheckPlugin.class);

        for (Project subproject : project.getSubprojects()) {
            applyPlugins(subproject);
        }
    }
}

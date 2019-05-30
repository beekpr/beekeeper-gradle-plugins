package io.beekeeper.formatter;

import java.nio.file.Paths;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.util.GradleVersion;

import com.diffplug.gradle.spotless.SpotlessExtension;
import com.diffplug.gradle.spotless.SpotlessPlugin;

public class FormatterPlugin implements Plugin<Project> {
    static final String IDENTIFIER = "io.beekeeper.gradle.plugins.formatter";
    static final private GradleVersion MIN_GRADLE_VERSION_SUPPORTED = GradleVersion.version("4.6");

    private static final String FORMATTING_CONF_FOLDER = "beekeeper-formatter-rules";
    private static final String JAVA_FORMATTING_RULES_RELATIVE_PATH = FORMATTING_CONF_FOLDER + "/java/BeekeeperCodeFormat.xml";

    @Override
    public void apply(Project project) {
        mandatoryGradleVersionCheck(project);

        project.getPluginManager().apply(SpotlessPlugin.class);

        applySpotlessJavaConfiguration(project);
        project.afterEvaluate(pr -> {
            applyFormatTask(project);
        });
    }

    private void mandatoryGradleVersionCheck(Project project) {
        GradleVersion currentGradleVersion = GradleVersion.version(project.getGradle().getGradleVersion());

        if (currentGradleVersion.compareTo(MIN_GRADLE_VERSION_SUPPORTED) < 0) {
            throw new GradleException("This version of the plugin is incompatible with gradle < 4.6!");
        }
    }

    private void applySpotlessJavaConfiguration(Project project) {
        if (!project.getPluginManager().hasPlugin("java")) {
            return;
        }

        SpotlessExtension extension = project.getExtensions().findByType(SpotlessExtension.class);
        if (extension == null) {
            throw new GradleException("Must have spotless plugin installed");
        }

        extension.java(java -> {
            java.removeUnusedImports();
            java.trimTrailingWhitespace();

            java.eclipse()
                .configFile(
                    getJavaFormattingRulesAbsoluteProjectPath(project)
                );
        });
    }

    private void applyFormatTask(Project project) {
        project.getTasks().create("extractBeekeeperFormattingConfig", ExtractResourceTask.class, (task) -> {
            task.setResourcePath(JAVA_FORMATTING_RULES_RELATIVE_PATH);
            task.setDestination(getJavaFormattingRulesAbsoluteProjectPath(project));
        });

        // Make the spotless tasks dependent on the config extraction task,
        // so they can reach the config files
        project.getTasksByName("spotlessCheck", true).iterator().next().dependsOn("extractBeekeeperFormattingConfig");
        project.getTasksByName("spotlessApply", true).iterator().next().dependsOn("extractBeekeeperFormattingConfig");
    }

    private String getJavaFormattingRulesAbsoluteProjectPath(Project project) {
        return Paths.get(project.getBuildDir().getAbsolutePath(), JAVA_FORMATTING_RULES_RELATIVE_PATH).toString();
    }
}

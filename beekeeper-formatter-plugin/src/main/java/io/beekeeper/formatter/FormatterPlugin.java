package io.beekeeper.formatter;

import java.nio.file.Paths;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.util.GradleVersion;

import com.diffplug.gradle.spotless.SpotlessExtension;
import com.diffplug.gradle.spotless.SpotlessPlugin;

public class FormatterPlugin implements Plugin<Project> {
    public static final String IDENTIFIER = "io.beekeeper.gradle.plugins.formatter";

    private static final String PLUGIN_JAVA = "java";
    private static final GradleVersion MIN_GRADLE_VERSION_SUPPORTED = GradleVersion.version("4.6");

    private static final String JAVA_FORMATTING_RULES_RELATIVE_PATH =
        "beekeeper-formatter-rules/java/BeekeeperCodeFormat.xml";

    @Override
    public void apply(Project project) {
        gradleVersionCheck(project);

        project.getPlugins().apply(SpotlessPlugin.class);

        applySpotlessJavaConfiguration(project);
        project.afterEvaluate(this::createFormatTask);
    }

    private void gradleVersionCheck(Project project) {
        GradleVersion currentGradleVersion = GradleVersion.version(project.getGradle().getGradleVersion());

        if (currentGradleVersion.compareTo(MIN_GRADLE_VERSION_SUPPORTED) < 0) {
            throw new GradleException("This version of the plugin is incompatible with gradle < 4.6!");
        }
    }

    private void applySpotlessJavaConfiguration(Project project) {

        SpotlessExtension extension = project.getExtensions().findByType(SpotlessExtension.class);
        if (extension == null) {
            throw new GradleException("Must have spotless plugin installed");
        }

        // The Java Spotless Extension can only be applied with the Java plugin
        // Otherwise, Spotless crashes. This is important as we may define
        // our plugin before the java plugin
        project.getPluginManager().withPlugin(PLUGIN_JAVA, javaPlugin -> {
            extension.java(java -> {
                java.removeUnusedImports();
                java.trimTrailingWhitespace();

                java.eclipse().configFile(getJavaRulesPath(project));
            });
        });
    }

    private void createFormatTask(Project project) {
        project.getTasks().create("extractBeekeeperFormattingConfig", ExtractResourceTask.class, (task) -> {
            task.setResourcePath(JAVA_FORMATTING_RULES_RELATIVE_PATH);
            task.setDestination(getJavaRulesPath(project));
        });

        // Make the spotless tasks dependent on the config extraction task,
        // so they can reach the config files
        project.getTasksByName("spotlessCheck", true).forEach(t -> t.dependsOn("extractBeekeeperFormattingConfig"));
        project.getTasksByName("spotlessApply", true).forEach(t -> t.dependsOn("extractBeekeeperFormattingConfig"));
    }

    private String getJavaRulesPath(Project project) {
        return Paths.get(project.getBuildDir().getAbsolutePath(), JAVA_FORMATTING_RULES_RELATIVE_PATH).toString();
    }
}

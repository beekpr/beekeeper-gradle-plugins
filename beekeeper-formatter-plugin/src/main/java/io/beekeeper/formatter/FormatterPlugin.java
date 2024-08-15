package io.beekeeper.formatter;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.util.GradleVersion;

import com.diffplug.gradle.spotless.SpotlessExtension;
import com.diffplug.gradle.spotless.SpotlessPlugin;

public class FormatterPlugin implements Plugin<Project> {
    public static final String IDENTIFIER = "io.beekeeper.gradle.plugins.formatter";

    private static final String PLUGIN_JAVA = "java";
    private static final String PLUGIN_GROOVY = "groovy";
    private static final GradleVersion MIN_GRADLE_VERSION_SUPPORTED = GradleVersion.version("4.6");

    @Override
    public void apply(Project project) {
        project.getExtensions().create(FormatOptions.EXTENSION, FormatOptions.class);

        gradleVersionCheck(project);

        project.getPlugins().apply(SpotlessPlugin.class);

        applySpotlessJavaConfiguration(project);
        applySpotlessGroovyConfiguration(project);
        applySpotlessGradleConfiguration(project);
    }

    public static class FormatOptions {
        static final String EXTENSION = "beekeeperCodeFormat";

        boolean useGoogleJavaFormat;
    }

    // TODO: Move it to the base gradle plugin
    private void gradleVersionCheck(Project project) {
        GradleVersion currentGradleVersion = GradleVersion.version(project.getGradle().getGradleVersion());

        if (currentGradleVersion.compareTo(MIN_GRADLE_VERSION_SUPPORTED) < 0) {
            throw new GradleException("This version of the plugin is incompatible with gradle < 5.6!");
        }
    }

    private void applySpotlessJavaConfiguration(Project project) {
        FormatOptions formatOptions = project.getExtensions().getByType(FormatOptions.class);
        SpotlessExtension extension = project.getExtensions().findByType(SpotlessExtension.class);
        if (extension == null) {
            throw new GradleException("Must have spotless plugin installed");
        }

        // The Java Spotless Extension can only be applied with the Java plugin
        // Otherwise, Spotless crashes. This is important as we may define
        // our plugin before the java plugin
        project.getPluginManager().withPlugin(PLUGIN_JAVA, javaPlugin -> {
            extension.java(java -> {
                java.target("**/*.java");
                java.removeUnusedImports();
                java.trimTrailingWhitespace();
                var formatConfig = formatOptions.useGoogleJavaFormat ? java.googleJavaFormat() : java.palantirJavaFormat();
            });
        });
    }

    private void applySpotlessGroovyConfiguration(Project project) {
        SpotlessExtension extension = project.getExtensions().findByType(SpotlessExtension.class);
        if (extension == null) {
            throw new GradleException("Must have spotless plugin installed");
        }

        // The Groovy Spotless Extension can only be applied with the Groovy plugin
        // Otherwise, Spotless crashes. This is important as we may define
        // our plugin before the goovy plugin
        project.getPluginManager().withPlugin(PLUGIN_GROOVY, javaPlugin -> {
            extension.groovy(groovy -> {
                groovy.trimTrailingWhitespace();
                groovy.indentWithSpaces(4);
            });
        });
    }

    private void applySpotlessGradleConfiguration(Project project) {
        SpotlessExtension extension = project.getExtensions().findByType(SpotlessExtension.class);
        if (extension == null) {
            throw new GradleException("Must have spotless plugin installed");
        }

        extension.groovyGradle(gradle -> {
            gradle.trimTrailingWhitespace();
            gradle.indentWithSpaces(4);
        });
    }
}


package io.beekeeper.gradle.quarkus;

import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.testing.Test;

import java.util.Arrays;
import java.util.Collections;

public class QuarkusPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.quarkus";

    @Override
    public void apply(Project project) {
        project.getExtensions().create(BeekeeperExtension.EXTENSION, BeekeeperExtension.class);

        // Get beekeeperPluginExtension
        project.getPluginManager().withPlugin("io.quarkus", it -> {
            project.afterEvaluate(this::applyJavaVersion);
        });

    }

    public static class BeekeeperExtension {
        static final String EXTENSION = "beekeeperJava";

        boolean java11;
        boolean java17;
        boolean java21;
    }

    private void applyJavaVersion(Project project) {
        BeekeeperExtension extension = project.getExtensions().getByType(BeekeeperExtension.class);

        if (extension.java11 || extension.java17 || extension.java21) {
            JavaCompile compileJava = (JavaCompile) project.getTasks().getByName("compileJava");
            compileJava.getOptions().setEncoding("UTF-8");
            compileJava.getOptions().setCompilerArgs(Collections.singletonList("-parameters"));

            JavaCompile compileTestJava = (JavaCompile) project.getTasks().getByName("compileTestJava");
            compileTestJava.getOptions().setEncoding("UTF-8");

            Test test = (Test) project.getTasks().getByName("test");
            test.systemProperty(
                "systemProperty",
                Arrays.asList("java.util.logging.manager", "org.jboss.logmanager.LogManager")
            );
        }

        JavaPluginConvention java = project.getConvention().getPlugin(JavaPluginConvention.class);
        if (extension.java11) {
            java.setSourceCompatibility(JavaVersion.VERSION_11);
            java.setTargetCompatibility(JavaVersion.VERSION_11);
        }

        if (extension.java17) {
            java.setSourceCompatibility(JavaVersion.VERSION_17);
            java.setTargetCompatibility(JavaVersion.VERSION_17);
        }

        if (extension.java21) {
            java.setSourceCompatibility(JavaVersion.VERSION_21);
            java.setTargetCompatibility(JavaVersion.VERSION_21);
        }

    }
}

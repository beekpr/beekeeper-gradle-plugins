package io.beekeeper.gradle.testing;

import java.util.List;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.testing.Test;
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension;
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension;
import org.gradle.testing.jacoco.tasks.JacocoReport;

public class TestingPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.testing";

    @Override
    public void apply(Project project) {
        applyJacoco(project);
    }

    private void applyJacoco(Project project) {
        project.getPluginManager().withPlugin("java", it -> {
            project.getPluginManager().apply("jacoco");
            JacocoPluginExtension jacoco = project.getExtensions().getByType(JacocoPluginExtension.class);
            jacoco.setToolVersion("0.8.8");

        });
        project.getPluginManager().withPlugin("io.quarkus", it -> {
            project.getDependencies().add("testImplementation", "io.quarkus:quarkus-jacoco");
        });

        project.afterEvaluate(this::configureJacoco);
    }

    private void configureJacoco(Project project) {
        final boolean hasQuarkus = project.getPluginManager().hasPlugin("io.quarkus");

        if (hasQuarkus) {
            configureQuarkusJacoco(project);
        } else {
            configureStandaloneJacoco(project);
        }
    }

    private void configureStandaloneJacoco(Project project) {
        project.getTasks().withType(JacocoReport.class, task -> {
            task.getReports().getXml().setEnabled(true);
            task.getReports().getCsv().setEnabled(true);
            task.getReports().getHtml().setEnabled(true);
        });
    }

    private void configureQuarkusJacoco(Project project) {
        // Quarkus-based projects use two versions of Jacoco:
        // - standalone version for unit tests
        // - Quarkus-provided version for tests annotated with @QuarkusTest
        // See: https://quarkus.io/guides/tests-with-coverage#coverage-for-tests-not-using-quarkustest
        // The Quarkus' version is further configured by the quarkus-beekeeper-api-convention extension.

        project.getTasks().withType(Test.class, task -> {
            final JacocoTaskExtension jacoco = task.getExtensions().getByType(JacocoTaskExtension.class);
            jacoco.setExcludeClassLoaders(List.of("*QuarkusClassLoader"));
            jacoco.setDestinationFile(project.getLayout().getBuildDirectory().file("reports/jacoco/test.exec").get().getAsFile());
        });

        project.getTasks().withType(JacocoReport.class, task -> {
            task.setEnabled(false);
        });
    }
}

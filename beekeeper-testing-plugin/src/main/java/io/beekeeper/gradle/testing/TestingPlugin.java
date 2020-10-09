package io.beekeeper.gradle.testing;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension;
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
            jacoco.setToolVersion("0.8.4");
        });

        project.afterEvaluate(this::configureJacoco);
    }

    private void configureJacoco(Project project) {
        project.getTasks().withType(JacocoReport.class, task -> {
            task.getReports().getXml().setEnabled(true);
            task.getReports().getCsv().setEnabled(true);
            task.getReports().getHtml().setEnabled(true);
        });
    }

}

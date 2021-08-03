package io.beekeeper.gradle.testing;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.testing.jacoco.tasks.JacocoReport;

public class SonarPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.sonarqube";

    @Override
    public void apply(Project project) { applySonar(project); }

    private void applySonar(Project project) {
        project.getPluginManager().withPlugin("org.sonarqube:3.0", it -> {
            project.getPluginManager().apply("sonarqube");
            project.getTasks().getByName("sonarqube").dependsOn("test");
        });
    }
}

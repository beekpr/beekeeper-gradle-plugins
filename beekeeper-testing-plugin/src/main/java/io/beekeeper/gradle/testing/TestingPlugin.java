package io.beekeeper.gradle.testing;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension;

public class TestingPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.testing";

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply("jacoco");
        JacocoPluginExtension jacoco = project.getExtensions().getByType(JacocoPluginExtension.class);

        jacoco.setToolVersion("0.8.4");
    }

}

package io.beekeeper.ide;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class IdePlugin implements Plugin<Project> {
    public static final String IDENTIFIER = "io.beekeeper.gradle.plugins.ide";

    @Override
    public void apply(Project project) {
        project.getPlugins().apply("eclipse");
        project.getPlugins().apply("idea");
    }

}

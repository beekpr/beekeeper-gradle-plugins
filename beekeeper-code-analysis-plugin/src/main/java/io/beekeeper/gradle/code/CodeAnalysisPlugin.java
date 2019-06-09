package io.beekeeper.gradle.code;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import com.github.spotbugs.SpotBugsExtension;
import com.github.spotbugs.SpotBugsPlugin;

public class CodeAnalysisPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.code-analysis-check";

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(SpotBugsPlugin.class);

        SpotBugsExtension config = project.getExtensions().getByType(SpotBugsExtension.class);
        config.setToolVersion("3.1.12");
    }

}

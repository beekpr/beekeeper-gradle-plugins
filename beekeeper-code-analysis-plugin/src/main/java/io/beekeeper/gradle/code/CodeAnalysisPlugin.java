package io.beekeeper.gradle.code;

import java.util.Collections;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

import com.github.spotbugs.SpotBugsExtension;
import com.github.spotbugs.SpotBugsPlugin;
import com.github.spotbugs.SpotBugsTask;

public class CodeAnalysisPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.code-analysis-check";

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(SpotBugsPlugin.class);

        SpotBugsExtension config = project.getExtensions().getByType(SpotBugsExtension.class);
        config.setToolVersion("3.1.12");

        project.getPluginManager().withPlugin("java", it -> {
            SourceSet main = project.getConvention()
                .getPlugin(JavaPluginConvention.class)
                .getSourceSets()
                .getByName("main");
            config.setSourceSets(Collections.singleton(main));
        });

        project.afterEvaluate(this::configureTasks);
    }

    private void configureTasks(Project project) {
        project.getTasks().withType(SpotBugsTask.class, task -> {
            task.getReports().getXml().setEnabled(true);
            task.getReports().getHtml().setEnabled(true);
            task.getReports().getEmacs().setEnabled(true);
            task.getReports().getText().setEnabled(true);
        });
    }

}

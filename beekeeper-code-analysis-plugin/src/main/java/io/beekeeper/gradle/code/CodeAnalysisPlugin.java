package io.beekeeper.gradle.code;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

import com.github.spotbugs.SpotBugsExtension;
import com.github.spotbugs.SpotBugsPlugin;
import com.github.spotbugs.SpotBugsTask;

public class CodeAnalysisPlugin implements Plugin<Project> {

    public static final String IDENTIFIER = "io.beekeeper.gradle.plugins.code-analysis-check";
    private static final String SPOT_BUGS_VERSION = "4.0.0";
    private static final String FIND_SEC_BUGS_VERSION = "1.10.1";
    private static final String QUARKUS_DEPENDENCY_GROUP = "io.quarkus";

    @Override
    public void apply(Project project) {
        project.getPluginManager().withPlugin("java", it -> {
            project.getPluginManager().apply(SpotBugsPlugin.class);

            SpotBugsExtension config = project.getExtensions().getByType(SpotBugsExtension.class);
            config.setToolVersion(SPOT_BUGS_VERSION);

            SourceSet main = project.getConvention()
                .getPlugin(JavaPluginConvention.class)
                .getSourceSets()
                .getByName("main");

            Map<String, String> findSecBugsDependency = new HashMap<>();
            findSecBugsDependency.put("group", "com.h3xstream.findsecbugs");
            findSecBugsDependency.put("name", "findsecbugs-plugin");
            findSecBugsDependency.put("version", FIND_SEC_BUGS_VERSION);

            project.getDependencies().add("spotbugsPlugins", findSecBugsDependency);

            config.setSourceSets(Collections.singleton(main));
        });

        project.afterEvaluate(this::configureTasks);
    }

    private void configureTasks(Project project) {
        project.getTasks().withType(SpotBugsTask.class, task -> {
            if (isQuarkusProject(project)) {
                task.setEnabled(false);
            } else {
                boolean isJenkins = isJenkins();
                task.getReports().getXml().setEnabled(isJenkins);
                task.getReports().getHtml().setEnabled(!isJenkins);
            }
        });
    }

    private boolean isJenkins() {
        return System.getenv("JENKINS_URL") != null;
    }

    private boolean isQuarkusProject(Project project) {
        return project
            .getConfigurations()
            .stream()
            .map(Configuration::getAllDependencies)
            .flatMap(Collection::stream)
            .anyMatch(d -> QUARKUS_DEPENDENCY_GROUP.equals(d.getGroup()));
    }
}

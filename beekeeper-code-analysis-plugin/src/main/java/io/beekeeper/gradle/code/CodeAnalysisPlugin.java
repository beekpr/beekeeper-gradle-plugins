package io.beekeeper.gradle.code;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;

import com.github.spotbugs.snom.SpotBugsExtension;
import com.github.spotbugs.snom.SpotBugsPlugin;
import com.github.spotbugs.snom.SpotBugsTask;

public class CodeAnalysisPlugin implements Plugin<Project> {

    public static final String IDENTIFIER = "io.beekeeper.gradle.plugins.code-analysis-check";
    private static final String SPOT_BUGS_VERSION = "4.1.3";
    private static final String FIND_SEC_BUGS_VERSION = "1.10.1";
    private static final String QUARKUS_DEPENDENCY_GROUP = "io.quarkus";

    @Override
    public void apply(Project project) {
        project.getPluginManager().withPlugin("java", it -> {
            project.getPluginManager().apply(SpotBugsPlugin.class);

            SpotBugsExtension config = project.getExtensions().getByType(SpotBugsExtension.class);
            config.getToolVersion().set(SPOT_BUGS_VERSION);

            Map<String, String> findSecBugsDependency = new HashMap<>();
            findSecBugsDependency.put("group", "com.h3xstream.findsecbugs");
            findSecBugsDependency.put("name", "findsecbugs-plugin");
            findSecBugsDependency.put("version", FIND_SEC_BUGS_VERSION);
            project.getDependencies().add("spotbugsPlugins", findSecBugsDependency);

            Map<String, String> spotbugsAnnotation = new HashMap<>();
            spotbugsAnnotation.put("group", "com.github.spotbugs");
            spotbugsAnnotation.put("name", "spotbugs-annotations");
            spotbugsAnnotation.put("version", config.getToolVersion().get());
            project.getDependencies().add("compileOnly", spotbugsAnnotation);
        });

        project.afterEvaluate(this::configureTasks);
    }

    private void configureTasks(Project project) {
        project.getTasks().withType(SpotBugsTask.class, task -> {
            // Only leave the Main sourceset as enabled
            if (!task.getName().equalsIgnoreCase("spotbugsMain")) {
                task.setEnabled(false);
                return;
            }

            // Currently we offer no support for SpotBugs for Quarkus
            if (isQuarkusProject(project)) {
                task.setEnabled(false);
                return;
            }

            boolean isJenkins = isJenkins();
            task.getReports().maybeCreate("xml").setEnabled(isJenkins);
            task.getReports().maybeCreate("html").setEnabled(!isJenkins);
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

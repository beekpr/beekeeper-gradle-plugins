
package io.beekeeper.gradle.quarkus;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.HashMap;
import java.util.Map;

public class QuarkusPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.quarkus";

    @Override
    public void apply(Project project) {
        addCoreDependencies(project);

        BeekeeperQuarkusExtension extension = project.getExtensions().create("beekeeper-quarkus", BeekeeperQuarkusExtension.class);

        if (extension.db) {
            addDbDependencies(project);
        }
        if (extension.rest) {
            addRestDependencies(project);
        }
    }

    public void addCoreDependencies(Project project) {
        Map<String, String> dependency = new HashMap<>();

        dependency.put("group", "io.quarkus");
        dependency.put("name", "quarkus-logging-gelf");
        project.getDependencies().add("quarkus-logging-gelf", dependency);

        dependency.put("group", "io.quarkus");
        dependency.put("name", "quarkus-logging-sentry");
        project.getDependencies().add("quarkus-logging-sentry", dependency);

        dependency.put("group", "org.jboss.slf4j");
        dependency.put("name", "slf4j-jboss-logging");
        project.getDependencies().add("slf4j-jboss-logging", dependency);
    }

    public void addDbDependencies(Project project) {
        Map<String, String> dependency = new HashMap<>();

        dependency.put("group", "io.quarkus");
        dependency.put("name", "quarkus-agroal");
        project.getDependencies().add("quarkus-agroal", dependency);

        dependency.put("group", "io.quarkus");
        dependency.put("name", "quarkus-jdbc-postgresql");
        project.getDependencies().add("quarkus-jdbc-postgresql", dependency);
    }

    public void addRestDependencies(Project project) {
        Map<String, String> dependency = new HashMap<>();

        dependency.put("group", "io.quarkus");
        dependency.put("name", "quarkus-resteasy");
        project.getDependencies().add("quarkus-resteasy", dependency);

        dependency.put("group", "io.quarkus");
        dependency.put("name", "quarkus-resteasy-jsonb");
        project.getDependencies().add("quarkus-resteasy-jsonb", dependency);
    }

    public static class BeekeeperQuarkusExtension {
        boolean db = true;
        boolean rest = true;
    }

}



package io.beekeeper.gradle.security;

import static org.owasp.dependencycheck.gradle.DependencyCheckPlugin.AGGREGATE_TASK;
import static org.owasp.dependencycheck.gradle.DependencyCheckPlugin.ANALYZE_TASK;
import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.HTML;
import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.JSON;
import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.XML;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin;
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension;

public class BeekeeperDependencyCheckPlugin implements Plugin<Project> {

    public static final String BEEKEEPER_PLUGIN_EXTENSION = StringUtils.uncapitalize(
        BeekeeperSecurityExtension.class.getSimpleName()

    );

    public static final String DEPENDENCY_CHECK_COMMON_SUPPRESSION_PATH = "dependency-check-common-suppression.xml";
    public static final String DEPENDENCY_CHECK_QUARKUS_SUPPRESSION_PATH = "dependency-check-quarkus-suppression.xml";

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.security.dependencyCheck";
    private static final String QUARKUS_DEPENDENCY_GROUP = "io.quarkus";

    @Override
    public void apply(Project project) {
        // Only apply this project on a parent. Never on sub-projects
        if (project.getParent() != null) {
            return;
        }

        project.getPluginManager().apply(DependencyCheckPlugin.class);

        DependencyCheckExtension config = project.getExtensions().getByType(DependencyCheckExtension.class);
        config.setFormats(
            List.of(
                HTML.toString().toLowerCase(),
                XML.toString().toLowerCase(),
                JSON.toString().toLowerCase()
            )
        );

        project.getExtensions().add(BEEKEEPER_PLUGIN_EXTENSION, new BeekeeperSecurityExtension());
        project.afterEvaluate(action -> {
            final BeekeeperSecurityExtension beekeeperSecurityExtension = project.getExtensions()
                .getByType(BeekeeperSecurityExtension.class);
            if (!beekeeperSecurityExtension.applyCommonSuppressions) {
                return;
            }
            prepareCommonSuppression(action, DEPENDENCY_CHECK_COMMON_SUPPRESSION_PATH, "Common");
            if (isQuarkusProject(project)) {
                prepareCommonSuppression(action, DEPENDENCY_CHECK_QUARKUS_SUPPRESSION_PATH, "Quarkus");
            }
        });
    }

    private void prepareCommonSuppression(Project project, String suppressionFileName, String groupName) {
        final String suppressionPathInBuildDir = Paths.get(
            project.getBuildDir().getAbsolutePath(),
            suppressionFileName
        ).toString();

        prepareAppendCommonSuppressionTask(project, suppressionPathInBuildDir, suppressionFileName, groupName);
        appendCommonSuppression(project, suppressionPathInBuildDir);
    }

    private void prepareAppendCommonSuppressionTask(
            Project project,
            String suppressionPathInBuildDir,
            String suppressionFileName,
            String groupName
    ) {
        final String appendCommonSuppression = String.format("appendSuppressions%s", groupName);
        project.getTasks().create(appendCommonSuppression, ExtractResourceTask.class, task -> {
            task.setDestination(suppressionPathInBuildDir);
            task.setResourcePath(suppressionFileName);
        });

        Stream.of(ANALYZE_TASK, AGGREGATE_TASK)
            .map(owaspTaskName -> project.getTasksByName(owaspTaskName, false))
            .flatMap(Collection::stream)
            .forEach(owaspTask -> owaspTask.dependsOn(appendCommonSuppression));
    }

    private void appendCommonSuppression(Project action, String commonSuppressionPath) {
        final DependencyCheckExtension dependencyCheckExtension = action.getExtensions()
            .getByType(DependencyCheckExtension.class);
        dependencyCheckExtension.getSuppressionFiles().add(commonSuppressionPath);
    }

    private boolean isQuarkusProject(Project project) {
        boolean hasQuarkusDependencies = project
            .getConfigurations()
            .stream()
            .map(Configuration::getAllDependencies)
            .flatMap(Collection::stream)
            .anyMatch(d -> QUARKUS_DEPENDENCY_GROUP.equals(d.getGroup()));

        if (hasQuarkusDependencies) {
            return true;
        }

        for (Project subProject : project.getSubprojects()) {
            if (isQuarkusProject(subProject)) {
                return true;
            }
        }

        return false;
    }

    public static class BeekeeperSecurityExtension {
        boolean applyCommonSuppressions = true;

        public void applyCommonSuppressions(boolean applyCommonSuppressions) {
            this.applyCommonSuppressions = applyCommonSuppressions;
        }
    }

}

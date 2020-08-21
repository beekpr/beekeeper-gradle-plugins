
package io.beekeeper.gradle.security;

import static org.owasp.dependencycheck.gradle.DependencyCheckPlugin.AGGREGATE_TASK;
import static org.owasp.dependencycheck.gradle.DependencyCheckPlugin.ANALYZE_TASK;
import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.HTML;
import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.JSON;
import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.XML;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin;
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension;

public class SecurityPlugin implements Plugin<Project> {


    public static final String BEEKEEPER_PLUGIN_EXTENSION = StringUtils.uncapitalize(
        BeekeeperSecurityExtension.class.getSimpleName()

    );

    public static final String DEPENDENCY_CHECK_COMMON_SUPPRESSION_PATH = "dependency-check-common-suppression.xml";
    public static final String DEPENDENCY_CHECK_QUARKUS_SUPPRESSION_PATH = "dependency-check-quarkus-suppression.xml";

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.security";

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(DependencyCheckPlugin.class);


        DependencyCheckExtension config = project.getExtensions().getByType(DependencyCheckExtension.class);
        config.setFormats(
            Arrays.asList(
                HTML,
                XML,
                JSON
            )
        );

        project.getExtensions().add(BEEKEEPER_PLUGIN_EXTENSION, new BeekeeperSecurityExtension());
        project.afterEvaluate(action -> {
            final BeekeeperSecurityExtension beekeeperSecurityExtension = project.getExtensions()
                .getByType(BeekeeperSecurityExtension.class);
            if (!beekeeperSecurityExtension.applyCommonSuppressions) {
                return;
            }
            skipSpotbugs(action);
            prepareCommonSuppression(action, DEPENDENCY_CHECK_COMMON_SUPPRESSION_PATH, "common");
            prepareCommonSuppression(action, DEPENDENCY_CHECK_QUARKUS_SUPPRESSION_PATH, "quarkus");
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
            .map(owaspTaskName -> project.getTasksByName(owaspTaskName, true))
            .flatMap(Collection::stream)
            .forEach(owaspTask -> owaspTask.dependsOn(appendCommonSuppression));
    }

    private void appendCommonSuppression(Project action, String commonSuppressionPath) {
        final DependencyCheckExtension dependencyCheckExtension = action.getExtensions()
            .getByType(DependencyCheckExtension.class);
        final List<String> suppressionFiles = dependencyCheckExtension.getSuppressionFiles();

        suppressionFiles.add(commonSuppressionPath);
    }

    public static class BeekeeperSecurityExtension {
        boolean applyCommonSuppressions = true;

        public void applyCommonSuppressions(boolean applyCommonSuppressions) {
            this.applyCommonSuppressions = applyCommonSuppressions;
        }
    }

    private void skipSpotbugs(Project action) {
        final DependencyCheckExtension dependencyCheckExtension = action.getExtensions()
            .getByType(DependencyCheckExtension.class);

        dependencyCheckExtension.getSkipConfigurations().add("spotbugs");
    }
}


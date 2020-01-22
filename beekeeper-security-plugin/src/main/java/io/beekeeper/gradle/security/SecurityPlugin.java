
package io.beekeeper.gradle.security;

import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.HTML;
import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.JSON;
import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.XML;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin;
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension;

public class SecurityPlugin implements Plugin<Project> {

    public static final String DEPENDENCY_CHECK_COMMON_SUPPRESSION_PATH = "/dependency-check-common-suppression.xml";
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

        project.getExtensions().add(BeekeeperSecurityExtension.class.getSimpleName(), new BeekeeperSecurityExtension());
        project.afterEvaluate(action -> {
            applyCommonSuppressionIfNeeded(action);
        });
    }

    private void applyCommonSuppressionIfNeeded(Project project) {
        final BeekeeperSecurityExtension beekeeperSecurityExtension = project.getExtensions().getByType(BeekeeperSecurityExtension.class);
        if (!beekeeperSecurityExtension.applyCommonSuppressions) {
            return;
        }
        final DependencyCheckExtension dependencyCheckExtension = project.getExtensions().getByType(DependencyCheckExtension.class);
        final List<String> suppressionFiles = dependencyCheckExtension.getSuppressionFiles();

        final String pathToSuppressionFile = getPathToCommonSuppressionFile(project);
        suppressionFiles.add(pathToSuppressionFile);
    }

    private String getPathToCommonSuppressionFile(Project action) {
        final URL resource = SecurityPlugin.class.getResource(DEPENDENCY_CHECK_COMMON_SUPPRESSION_PATH);
        if (resource.getProtocol().equals("jar")) {
            try {
                final JarURLConnection urlConnection;
                urlConnection = (JarURLConnection) resource.openConnection();
                final File file = action.getResources().getText().fromArchiveEntry(urlConnection.getJarFileURL().getFile(), urlConnection.getEntryName()).asFile();
                return file.getAbsolutePath();
            } catch (IOException e) {
                throw new IllegalArgumentException("Unable to set up common suppression file",e);
            }
        } else {
            throw new IllegalArgumentException("Unable to set up common suppression file");

        }
    }

    public static class BeekeeperSecurityExtension {
        boolean applyCommonSuppressions = true;

        public void applyCommonSuppressions(boolean applyCommonSuppressions) {
            this.applyCommonSuppressions = applyCommonSuppressions;
        }
    }
}


package io.beekeeper.gradle.security;

import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.HTML;
import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.JSON;
import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.XML;

import java.util.Arrays;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.owasp.dependencycheck.gradle.DependencyCheckPlugin;
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension;

public class SecurityPlugin implements Plugin<Project> {

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
    }

}

package io.beekeeper.gradle.licenses;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import com.github.jk1.license.LicenseReportExtension;
import com.github.jk1.license.LicenseReportPlugin;

public class LicenseCheckPlugin implements Plugin<Project> {

    public static final String IDENTIFIER = "io.beekeeper.gradle.plugins.license-check";

    @Override
    public void apply(Project project) {
        if (project.getParent() != null) {
            // Only applied to parent as the license report plugin already
            // supports sub-projects out of the box
            return;
        }
        project.getPluginManager().apply(LicenseReportPlugin.class);

        LicenseReportExtension extension = project.getExtensions().getByType(LicenseReportExtension.class);
        extension.allowedLicensesFile = getAllowedLicensesFile();
    }

    private File getAllowedLicensesFile() {
        try {
            Path path = Files.createTempFile("license-check-allowed-licenses", ".json");
            Files.copy(
                LicenseCheckPlugin.class.getResourceAsStream("allowed-licenses.json"),
                path,
                StandardCopyOption.REPLACE_EXISTING
            );
            File file = path.toFile();
            file.deleteOnExit();
            return file;
        } catch (IOException e) {
            throw new GradleException("Unexpected error: " + e.getMessage(), e);
        }
    }
}

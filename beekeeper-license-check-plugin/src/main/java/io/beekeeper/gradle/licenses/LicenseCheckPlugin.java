package io.beekeeper.gradle.licenses;

import java.io.File;

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
        return new File(LicenseCheckPlugin.class.getResource("allowed-licenses.json").getFile());
    }
}

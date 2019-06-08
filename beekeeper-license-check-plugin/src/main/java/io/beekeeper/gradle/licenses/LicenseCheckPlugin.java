package io.beekeeper.gradle.licenses;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.Nested;

import com.github.jk1.license.LicenseReportExtension;
import com.github.jk1.license.LicenseReportPlugin;
import com.github.jk1.license.ProjectData;
import com.github.jk1.license.reader.ProjectReader;


public class LicenseCheckPlugin implements Plugin<Project> {

    public static final String IDENTIFIER = "io.beekeeper.gradle.plugins.license-check";

    @Nested
    LicenseReportExtension getConfig(Project project) {
        return (LicenseReportExtension) project.getExtensions().findByName("licenseReport");
    }

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(LicenseReportPlugin.class);
        ProjectData data = new ProjectReader(getConfig(project)).read(project);

        data.getAllDependencies().forEach(moduleData -> {
            project.getLogger()
                .info("{}:{}, licenses: {}", moduleData.getGroup(), moduleData.getName(), moduleData.getLicenseFiles());
        });
    }
}

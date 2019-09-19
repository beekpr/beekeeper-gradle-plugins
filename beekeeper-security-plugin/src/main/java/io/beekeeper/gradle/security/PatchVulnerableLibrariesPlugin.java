package io.beekeeper.gradle.security;

import javax.annotation.Nonnull;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.DependencyResolveDetails;
import org.gradle.api.artifacts.ModuleVersionSelector;

public class PatchVulnerableLibrariesPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.security.patcher";

    private static final ModulePatcher JACKSON_DATABIND = new ModulePatcher(
            "com.fasterxml.jackson.core",
            "jackson-databind",
            "2.10.0.pr3",
            "CVE-2019-16335, CVE-2019-14540"
    );

    @Override
    public void apply(Project project) {
        project.getConfigurations().forEach(configuration -> {
            configuration.resolutionStrategy(strategy -> {
                strategy.eachDependency(details -> {
                    JACKSON_DATABIND.patch(details);
                });
            });
        });
    }

    public static class ModulePatcher {
        private final String group;
        private final String name;
        private final String versionToUse;
        private final String reason;

        public ModulePatcher(
                @Nonnull String group,
                @Nonnull String name,
                @Nonnull String versionToUse,
                @Nonnull String reason
        ) {
            this.group = group;
            this.name = name;
            this.versionToUse = versionToUse;
            this.reason = reason;
        }

        public boolean matches(ModuleVersionSelector moduleVersionSelector) {
            return group.equals(moduleVersionSelector.getGroup())
                && name.equals(moduleVersionSelector.getName());
        }

        public void patch(DependencyResolveDetails details) {
            if (this.matches(details.getRequested())) {
                details.useVersion(versionToUse);
                details.because(reason);
            }
        }
    }

}

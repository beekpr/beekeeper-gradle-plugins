package io.beekeeper.gradle.security;

import org.cyclonedx.gradle.CycloneDxPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;

public class BeekeeperCycloneDxPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.security.cyclonedx";

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(CycloneDxPlugin.class);

        Provider<LockService> lockService = project.getGradle()
            .getSharedServices()
            .registerIfAbsent(
                "lockService",
                LockService.class,
                (spec) -> {
                    spec.getMaxParallelUsages().set(1);
                }
            );

        project.getTasks().getByPath("cyclonedxBom").usesService(lockService);
    }

    public static class LockService implements BuildService<BuildServiceParameters.None> {
        @Override
        public BuildServiceParameters.None getParameters() {
            return null;
        }
    }

}

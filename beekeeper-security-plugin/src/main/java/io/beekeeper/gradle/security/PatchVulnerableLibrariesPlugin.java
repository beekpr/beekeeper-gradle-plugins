package io.beekeeper.gradle.security;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import io.beekeeper.gradle.security.patches.PatchedModuleResolver;
import io.beekeeper.gradle.security.patches.PatchedModules;

public class PatchVulnerableLibrariesPlugin implements Plugin<Project> {

    public static String IDENTIFIER = "io.beekeeper.gradle.plugins.security.patcher";

    @Override
    public void apply(Project project) {
        project.afterEvaluate(this::patchModules);
    }

    public void patchModules(Project project) {
        PatchedModuleResolver resolver = new PatchedModuleResolver(PatchedModules.ALL);
        project.getConfigurations().forEach(configuration -> {
            configuration.resolutionStrategy(strategy -> {
                strategy.eachDependency(details -> {
                    resolver.find(details.getRequested()).ifPresent(it -> it.patch(details));
                });
            });
        });
    }

}

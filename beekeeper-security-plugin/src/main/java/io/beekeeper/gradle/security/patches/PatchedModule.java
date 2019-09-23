package io.beekeeper.gradle.security.patches;

import javax.annotation.Nonnull;

import org.gradle.api.artifacts.DependencyResolveDetails;
import org.gradle.api.artifacts.ModuleVersionSelector;

public class PatchedModule {
    public final String group;
    public final String name;
    private final String versionToUse;
    private final String reason;

    public PatchedModule(
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

package io.beekeeper.gradle.security.patches;

import org.gradle.api.artifacts.DependencyResolveDetails;
import org.gradle.api.artifacts.ModuleVersionSelector;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        if (!this.matches(details.getRequested())) {
            return;
        }
        if (this.isLowerThan(details.getRequested())) {
            return;
        }
        details.useVersion(versionToUse);
        details.because(reason);
    }

    private boolean isLowerThan(ModuleVersionSelector requested) {
        SemVer originalVersion = SemVer.of(requested.getVersion());
        SemVer patchedVersion = SemVer.of(versionToUse);

        return patchedVersion.compareTo(originalVersion) < 0;
    }


    public static class SemVer implements Comparable {
        private final List<String> elements;

        private SemVer(List<String> elements) {
            this.elements = elements;
        }

        public static SemVer of(String version) {
            final String[] elements = version.split("\\.");
            return new SemVer(Arrays.asList(elements));
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof SemVer)) {
                return 0;
            }

            SemVer other = (SemVer) o;
            final int size = Math.min(elements.size(), other.elements.size());
            int compareResult = 0;
            int index = 0;
            while (index < size && compareResult == 0) {
                final Optional<Integer> thisVersionAt = numericVersionAt(index);
                final Optional<Integer> otherVersionAt = other.numericVersionAt(index);
                if (!thisVersionAt.isPresent() || !otherVersionAt.isPresent()) {
                    return 0;
                }
                compareResult = thisVersionAt.get().compareTo(otherVersionAt.get());
                ++index;
            }
            return compareResult;
        }

        public Optional<Integer> numericVersionAt(Integer index) {
            try {
                return Optional.of(Integer.parseInt(elements.get(index)));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }

        }
    }
}

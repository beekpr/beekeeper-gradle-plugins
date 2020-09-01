package io.beekeeper.gradle.security.patches;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.gradle.api.artifacts.ModuleVersionSelector;

public class PatchedModuleResolver {

    private final Map<String, PatchedModule> modules;

    public PatchedModuleResolver(Collection<PatchedModule> m) {
        modules = m.stream().collect(Collectors.toMap(module -> Key.from(module), module -> module));
    }

    public Optional<PatchedModule> find(ModuleVersionSelector selector) {
        return Optional.ofNullable(modules.get(Key.from(selector)));
    }

    private static class Key {
        @SuppressFBWarnings("UPM_UNCALLED_PRIVATE_METHOD")
        private static String from(PatchedModule module) {
            return from(module.group, module.name);
        }

        public static String from(ModuleVersionSelector selector) {
            return from(selector.getGroup(), selector.getName());
        }

        private static String from(String group, String name) {
            return group + ":" + name;
        }
    }
}

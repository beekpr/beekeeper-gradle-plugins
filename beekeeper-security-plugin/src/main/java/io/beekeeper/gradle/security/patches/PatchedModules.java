package io.beekeeper.gradle.security.patches;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PatchedModules {

    static final Collection<PatchedModule> ALL = Collections.unmodifiableCollection(
        Arrays.asList(
            new PatchedModule(
                    "com.fasterxml.jackson.core",
                    "jackson-databind",
                    "2.10.0.pr3",
                    "CVE-2019-16335, CVE-2019-14540"
            )
        )
    );
}

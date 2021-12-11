package io.beekeeper.gradle.security.patches;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PatchedModules {

    public static final Collection<PatchedModule> ALL = Collections.unmodifiableCollection(
        Arrays.asList(
            new PatchedModule(
                    "com.fasterxml.jackson.core",
                    "jackson-databind",
                    "2.10.0.pr3",
                    "CVE-2019-16335, CVE-2019-14540"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j",
                    "2.15.0",
                    "CVE-2021-44228"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-core",
                    "2.15.0",
                    "CVE-2021-44228"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-api",
                    "2.15.0",
                    "CVE-2021-44228"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-1.2-api",
                    "2.15.0",
                    "CVE-2021-44228"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-slf4j-impl ",
                    "2.15.0",
                    "CVE-2021-44228"
            )
        )
    );
}

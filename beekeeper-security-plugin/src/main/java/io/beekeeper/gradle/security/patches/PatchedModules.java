package io.beekeeper.gradle.security.patches;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PatchedModules {

    private static final String LOG4J_MIN_VERSION = "2.17.0";

    public static final Collection<PatchedModule> ALL = Collections.unmodifiableCollection(
        Arrays.asList(
            new PatchedModule(
                    "com.fasterxml.jackson.core",
                    "jackson-databind",
                    "2.10.0.pr3",
                    "CVE-2019-16335, CVE-2019-14540"
            ),
            // Log4J
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-core",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-api",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-1.2-api",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-slf4j-impl",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-to-slf4j",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-layout-template-json",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-jcl",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-flume-ng",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-taglib",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-jmx-gui",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-slf4j18-impl",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-appserver",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-web",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-couchdb",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-mongodb4",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-mongodb3",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-cassandra",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-jpa",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-iostreams",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-jul",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-jpl",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-liquibase",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-docker",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-kubernetes",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-spring-boot",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            ),
            new PatchedModule(
                    "org.apache.logging.log4j",
                    "log4j-spring-cloud-config-client",
                    LOG4J_MIN_VERSION,
                    "CVE-2021-44228, CVE-2021-45105"
            )
        )
    );
}

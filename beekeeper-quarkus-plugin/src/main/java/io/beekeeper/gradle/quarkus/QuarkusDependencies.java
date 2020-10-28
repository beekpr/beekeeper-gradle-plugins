package io.beekeeper.gradle.quarkus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuarkusDependencies {
    public static Map<String, List<String>> getDependencies() {
        List<String> core = Arrays.asList(
            "io.quarkus:quarkus-logging-gelf",
            "io.quarkus:quarkus-logging-sentry",
            "org.jboss.slf4j:slf4j-jboss-logging",
            "io.quarkus:quarkus-smallrye-health",
            "io.quarkus:quarkus-micrometer",
            "io.micrometer:micrometer-registry-prometheus"
        );

        List<String> db = Arrays.asList(
            "io.quarkus:quarkus-agroal",
            "io.quarkus:quarkus-jdbc-postgresql"
        );

        List<String> service = Arrays.asList(
            "io.quarkus:quarkus-logging-gelf",
            "io.quarkus:quarkus-logging-sentry",
            "org.jboss.slf4j:slf4j-jboss-logging",
            "io.quarkus:quarkus-smallrye-health",
            "io.quarkus:quarkus-micrometer",
            "io.micrometer:micrometer-registry-prometheus",

            "io.quarkus:quarkus-resteasy",
            "io.quarkus:quarkus-resteasy-jackson",
            "org.zalando:problem:0.25.0",
            "org.zalando:jackson-datatype-problem:0.25.0",
            "io.beekeeper:quarkus-exception-mapper:0.2",
            "io.quarkus:quarkus-smallrye-openapi"
        );

        List<String> restClient = Arrays.asList(
            "io.quarkus:quarkus-rest-client",
            "io.quarkus:quarkus-rest-client-jackson"
        );

        HashMap<String, List<String>> dependencies = new HashMap<>();
        dependencies.put("core", core);
        dependencies.put("db", db);
        dependencies.put("service", service);
        dependencies.put("restClient", restClient);

        return dependencies;
    }
}

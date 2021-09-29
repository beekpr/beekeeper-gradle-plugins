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
            "io.quarkus:quarkus-smallrye-health",
            "io.quarkus:quarkus-micrometer-registry-prometheus"
        );

        List<String> db = Arrays.asList(
            "io.quarkus:quarkus-agroal",
            "io.quarkus:quarkus-liquibase",
            "io.quarkus:quarkus-jdbc-postgresql"
        );

        List<String> service = Arrays.asList(
            "io.quarkus:quarkus-resteasy",
            "io.quarkus:quarkus-resteasy-jackson",
            "org.zalando:problem",
            "org.zalando:jackson-datatype-problem",
            "io.beekeeper:quarkus-beekeeper-api-convention",
            "io.quarkus:quarkus-smallrye-openapi"
        );

        List<String> restClient = Arrays.asList(
            "io.quarkus:quarkus-rest-client",
            "io.quarkus:quarkus-rest-client-jackson"
        );

        List<String> eventBus = Arrays.asList(
            "io.quarkus:quarkus-smallrye-reactive-messaging",
            "io.quarkus:quarkus-smallrye-reactive-messaging-kafka"
        );

        HashMap<String, List<String>> dependencies = new HashMap<>();
        dependencies.put("core", core);
        dependencies.put("db", db);
        dependencies.put("service", service);
        dependencies.put("restClient", restClient);
        dependencies.put("eventBus", eventBus);

        return dependencies;
    }
}

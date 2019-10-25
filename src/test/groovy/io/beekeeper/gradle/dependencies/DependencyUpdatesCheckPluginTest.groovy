package io.beekeeper.gradle.dependencies

import io.beekeeper.gradle.testing.GradleWorkspace
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import spock.lang.Specification

class DependencyUpdatesCheckPluginTest extends Specification {

    @Rule
    GradleWorkspace workspace

    GradleRunner runner

    def setup() {
        runner = workspace
            .runner
            .withPluginClasspath()

        workspace.buildFile << """
        plugins {
            id 'java'
            id '${DependencyUpdatesCheckPlugin.IDENTIFIER}'
        }
        """
    }

    def "it should apply the spotbugs plugin"() {
        when:
        BuildResult result = runner.withArguments('tasks', '--all').build()

        then:
        result.output.contains("dependencyUpdates")
    }
}

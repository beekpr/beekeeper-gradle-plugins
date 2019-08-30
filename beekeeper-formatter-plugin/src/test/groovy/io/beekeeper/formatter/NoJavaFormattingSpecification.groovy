package io.beekeeper.formatter

import io.beekeeper.gradle.testing.GradleWorkspace
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import spock.lang.Specification

class NoJavaFormattingSpecification extends Specification {

    @Rule
    GradleWorkspace workspace

    GradleRunner runner

    def setup() {
        runner = workspace
            .runner
            .withPluginClasspath()

        workspace.buildFile << """
        plugins {
            id '${FormatterPlugin.IDENTIFIER}'
        }
        """
    }

    def "it should NOT apply the spotless java plugin in a non-java project"() {
        when:
        BuildResult result = runner.withArguments('tasks', '--all').build()

        then:
        !result.output.contains("spotlessJava")
    }
}

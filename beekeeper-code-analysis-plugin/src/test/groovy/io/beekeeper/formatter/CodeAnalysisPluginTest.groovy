package io.beekeeper.formatter

import io.beekeeper.gradle.code.CodeAnalysisPlugin
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule

import io.beekeeper.gradle.testing.GradleWorkspace
import spock.lang.Specification

class CodeAnalysisPluginTest extends Specification {

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
            id '${CodeAnalysisPlugin.IDENTIFIER}'
        }
        """
    }

    def "it should apply the spotbugs plugin"() {
        when:
        BuildResult result = runner.withArguments('tasks', '--all').build()

        then:
        result.output.contains("spotbugsMain")
    }
}

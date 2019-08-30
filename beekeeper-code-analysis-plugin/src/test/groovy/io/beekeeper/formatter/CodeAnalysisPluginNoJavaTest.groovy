package io.beekeeper.formatter

import io.beekeeper.gradle.code.CodeAnalysisPlugin
import io.beekeeper.gradle.testing.GradleWorkspace
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import spock.lang.Specification

class CodeAnalysisPluginNoJavaTest extends Specification {

    @Rule
    GradleWorkspace workspace

    GradleRunner runner

    def setup() {
        runner = workspace
            .runner
            .withPluginClasspath()

        workspace.buildFile << """
        plugins {
            id '${CodeAnalysisPlugin.IDENTIFIER}'
        }
        """
    }

    def "it should NOT apply the spotbugs plugin in a non-java project"() {
        when:
        BuildResult result = runner.withArguments('tasks', '--all').build()

        then:
        !result.output.contains("spotbugsMain")
    }
}

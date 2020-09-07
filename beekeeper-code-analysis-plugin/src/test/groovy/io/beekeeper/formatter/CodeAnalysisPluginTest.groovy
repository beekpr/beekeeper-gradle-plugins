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

    def setup(boolean quarkus) {
        runner = workspace
            .runner
            .withPluginClasspath()

        if (quarkus) {
            workspace.buildFile << """
                    plugins {
                        id 'java'
                        id '${CodeAnalysisPlugin.IDENTIFIER}'
                    }
                    """
        } else {
            workspace.buildFile << """
                    plugins {
                        id 'java'
                        id '${CodeAnalysisPlugin.IDENTIFIER}'
                    }

                    dependencies {
                        compile "io.quarkus:quarkus-resteasy"
                        implementation "io.quarkus:quarkus-kubernetes"
                    }
                    """
                    }

    }

    def "it should apply the spotbugs plugin"() {
        given:
        setup(false)

        when:
        BuildResult result = runner.withArguments('tasks', '--all').build()

        then:
        result.output.contains("spotbugsMain")
    }

    def "it should not apply the spotbugs plugin for Quarkus projects"() {
        given:
        setup(true)

        when:
        BuildResult result = runner.withArguments('tasks', '--all').build()

        then:
        result.output.contains("spotbugsMain")
    }
}

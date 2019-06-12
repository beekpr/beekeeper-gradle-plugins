package io.beekeeper.formatter

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule

import io.beekeeper.gradle.testing.GradleWorkspace
import spock.lang.Specification

class JavaFormattingSpecification extends Specification {

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
            id '${FormatterPlugin.IDENTIFIER}'
        }
        """
    }

    def "it should apply the spotless plugin"() {
        when:
        BuildResult result = runner.withArguments('tasks').build()

        then:
        result.output.contains("spotlessApply")
    }

    def "it should format a trivial java file" () {
        given:
        def snip = workspace.file("src/main/java/Snip.java")

        snip << """class Snip { public static void main(String ... args) { System.out.println("Snip"); } }"""

        when:
        BuildResult result = runner.withArguments('spotlessApply').build()

        then:
        result.getOutput().contains("spotless")
        result.tasks.every { task -> task.outcome == TaskOutcome.SUCCESS }

        def lines = workspace.file("src/main/java/Snip.java").readLines()
        lines.size() > 1
        // at least some lines should be properly indented now
        lines.any { line -> line.startsWith("    ") }
    }
}

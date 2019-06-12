package io.beekeeper.formatter

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.junit.Rule

import io.beekeeper.gradle.testing.GradleWorkspace
import spock.lang.Specification
import spock.lang.Unroll

class GradleVersionCompatibilitySpec extends Specification {

    @Rule
    GradleWorkspace workspace
    GradleRunner runner

    static final List<String> supportedVersions = [
        '4.6',
        '4.7',
        '4.8',
        '4.8.1',
        '4.9',
        '4.10',
        "5.0",
        '5.1'
    ]
    static final List<String> unsupportedVersions = ['4.5.1', '4.4.1', '3.5']

    def setup() {
        runner = workspace.runner.withPluginClasspath()

        workspace.buildFile << """
        buildscript {
            repositories { mavenCentral() }
        }
        plugins {
            id 'java'
            id '${FormatterPlugin.IDENTIFIER}'
        }
        """
    }

    @Unroll
    def 'plugin nicely applies with gradle #gradleVersion'() {
        given:
        runner = runner.withGradleVersion(gradleVersion)

        workspace.buildFile << """
        task foo {
            doLast {
                println "Formatting completed"
            }
        }
        """

        when:
        BuildResult result = runner.withArguments('foo').build()

        then:
        assert result.output.contains('Formatting completed')

        where:
        gradleVersion << supportedVersions
    }

    @Unroll
    def "plugin should reformat a source folder with some java code with #gradleVersion" () {
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

        where:
        gradleVersion << supportedVersions
    }

    @Unroll
    def 'plugin should fail with a nice exception when using gradle #gradleVersion'() {
        given:
        runner = runner.withGradleVersion(gradleVersion)

        when:
        runner.build()

        then:
        def exception = thrown(UnexpectedBuildFailure)

        assert exception.message.contains('This version of the plugin is incompatible with gradle < 4.6!')

        where:
        gradleVersion << unsupportedVersions
    }

}
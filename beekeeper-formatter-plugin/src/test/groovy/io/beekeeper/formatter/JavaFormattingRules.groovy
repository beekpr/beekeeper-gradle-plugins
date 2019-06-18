package io.beekeeper.formatter

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule

import io.beekeeper.gradle.testing.GradleWorkspace
import spock.lang.Specification
import spock.lang.Unroll

class JavaFormattingRules extends Specification {

    @Rule
    GradleWorkspace workspace

    GradleRunner runner

    def setup() {
        runner = workspace
            .runner
            .withPluginClasspath()

        workspace.buildFile << """
        |plugins {
        |    id 'java'
        |    id '${FormatterPlugin.IDENTIFIER}'
        |}""".stripMargin()
    }

    @Unroll
    def "the code style is in line with the files provided in #sourceFile"() {
        given:
        def resourceName = this.class.name.replace('.', '/') + "_OK_$sourceFile"
        workspace.file("src/main/java/io/beekeeper/formatter/$sourceFile") <<
            this.class.classLoader.getResource(resourceName).text


        when:
        BuildResult result = runner.withArguments('spotlessCheck').build()

        then:
        result.tasks.every { task -> task.outcome == TaskOutcome.SUCCESS }

        where:
        sourceFile << [
            'Builder.java',
            'EmptyClass.java',
            'LongLines.java',
            'Simple.java',
        ]
    }
}

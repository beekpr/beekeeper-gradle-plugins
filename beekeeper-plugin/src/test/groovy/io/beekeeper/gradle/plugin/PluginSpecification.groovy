package io.beekeeper.gradle.plugin

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class PluginSpecification extends Specification {

    @Rule
    TemporaryFolder dir
    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(dir.root)
            .withPluginClasspath()

        buildFile << """
        plugins {
            id 'java'
            id '${BeekeeperPlugin.IDENTIFIER}'
        }

        beekeeper {
            requiredVersionUrl = 'file://${minimumVersionFile.path}'
        }
        """
    }

    def "it should expose the beekeeperPluginCheck task"() {
        when:
        BuildResult result = runner.withArguments('tasks').build()

        then:
        result.output.contains("beekeeperPluginCheck")
    }

    def "beekeeperPluginCheck should succeed with minimum version < current"() {
        given:
        minimumVersionFile << """0.1.0"""

        when:
        BuildResult result = runner
            .withArguments('beekeeperPluginCheck', '--stacktrace')
            .build()

        then:
        result.task(":beekeeperPluginCheck").outcome == SUCCESS
    }

    def "beekeeperPluginCheck should fail with minimum version > current"() {
        given:
        minimumVersionFile << """99.0.0"""

        when:
        BuildResult result = runner
            .withArguments('beekeeperPluginCheck', '--stacktrace')
            .buildAndFail()

        then:
        result.task(":beekeeperPluginCheck").outcome == FAILED
    }

    File getBuildFile() {
        file('build.gradle')
    }

    File getSettingsFile() {
        file('settings.gradle')
    }

    File getMinimumVersionFile() {
        file('min')
    }

    File file(String path) {
        File f = new File(dir.root, path)
        if (!f.exists()) {
            f.parentFile.mkdirs()
            return dir.newFile(path)
        }
        return f
    }
}
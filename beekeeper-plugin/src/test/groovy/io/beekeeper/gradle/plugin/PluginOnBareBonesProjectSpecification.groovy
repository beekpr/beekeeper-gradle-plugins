package io.beekeeper.gradle.plugin

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class PluginOnBareBonesProjectSpecification extends Specification {

    @Rule
    TemporaryFolder dir
    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(dir.root)
            .withPluginClasspath()

        buildFile << """
        plugins {
            id '${BeekeeperPlugin.IDENTIFIER}'
        }
        """
    }

    def "it should apply nicely to a bare bones project"() {
        when:
        BuildResult result = runner.withArguments('tasks').build()

        then:
        result.output.contains("beekeeperPluginCheck")
    }

    File getBuildFile() {
        file('build.gradle')
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
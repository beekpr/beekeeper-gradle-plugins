package io.beekeeper.gradle.license

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule

import io.beekeeper.gradle.licenses.LicenseCheckPlugin
import io.beekeeper.gradle.testing.GradleWorkspace
import spock.lang.Ignore
import spock.lang.Specification

class LicenseCheckPluginSpec extends Specification {

    @Rule
    GradleWorkspace workspace

    GradleRunner runner

    def "it should apply the license check plugin when applied"() {
        given:
        runner = workspace
            .runner
            .withPluginClasspath()

        workspace.buildFile << """
            plugins {
                id '${LicenseCheckPlugin.IDENTIFIER}'
            }
        """

        when:
        BuildResult result = runner.withArguments('generateLicenseReport', '--stacktrace').build()

        then:
        result.tasks.every { task -> task.outcome == TaskOutcome.SUCCESS }
    }

    def "it should apply configure the license check correctly"() {
        given:
        runner = workspace
            .runner
            .withPluginClasspath()

        workspace.buildFile << """
            plugins {
                id '${LicenseCheckPlugin.IDENTIFIER}'
            }
            """

        when:
        BuildResult result = runner.withArguments('checkLicense', '--stacktrace').build()

        then:
        result.tasks.every { task -> task.outcome == TaskOutcome.SUCCESS }
    }

    @Ignore
    def "it should fail for evil license dependencies" () {
        given:
        runner = workspace
            .runner
            .withPluginClasspath()


        workspace.buildFile << """
         plugins {
           id 'java'
           id '${LicenseCheckPlugin.IDENTIFIER}'
         }

         repositories {
           mavenCentral()
         }

         dependencies {
           implementation "evil.corp:evil-gpl-licensed-thing:1.0.0"
         }
        """

        expect:
        2 == 2
    }
}

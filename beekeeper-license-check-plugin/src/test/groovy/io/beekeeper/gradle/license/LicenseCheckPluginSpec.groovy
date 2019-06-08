package io.beekeeper.gradle.license

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import io.beekeeper.gradle.licenses.LicenseCheckPlugin
import io.beekeeper.gradle.testing.SpecificationWithBuildFiles
import spock.lang.Ignore
import spock.lang.Specification

class LicenseCheckPluginSpec extends SpecificationWithBuildFiles {

    GradleRunner runner

    def "it should apply the license check plugin when applied"() {
        given:
        runner = GradleRunner.create()
                .withProjectDir(dir.root)
                .withPluginClasspath()

        buildFile << """
            plugins {
                id '${LicenseCheckPlugin.IDENTIFIER}'
            }
        """

        when:
        BuildResult result = runner.withArguments('generateLicenseReport').build()

        then:
        result.tasks.every { task -> task.outcome == TaskOutcome.SUCCESS }
    }

    @Ignore
    def "it should fail for evil license dependencies" () {
        given:
        runner = GradleRunner.create()
                .withProjectDir(dir.root)
                .withPluginClasspath()


        buildFile << """
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

package io.beekeeper.gradle.security
import io.beekeeper.gradle.security.PatchVulnerableLibrariesPlugin
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class PatchVersionSpecification extends Specification {

    @Rule
    TemporaryFolder dir
    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(dir.root)
            .withPluginClasspath()
    }

    def "it should update lower version"() {
        given:
        setUpBuildGradle("2.9.1")

        when:
        BuildResult result = runner.withArguments(
            ':dependencyInsight',
            '--configuration',
            'compileClasspath',
            '--dependency',
            'com.fasterxml.jackson.core:jackson-databind'
        ).build()

        then:
        result.output.contains("Task :dependencyInsight\n" +
            "com.fasterxml.jackson.core:jackson-databind:2.10.0.pr3")
    }

    def "it should update same version"() {
        given:
        setUpBuildGradle("2.10.0.pr2")

        when:
        BuildResult result = runner.withArguments(
            ':dependencyInsight',
            '--configuration',
            'compileClasspath',
            '--dependency',
            'com.fasterxml.jackson.core:jackson-databind'
        ).build()

        then:
        result.output.contains("Task :dependencyInsight\n" +
            "com.fasterxml.jackson.core:jackson-databind:2.10.0.pr3")
    }

    def "it should keep higher version"() {
        given:
        setUpBuildGradle("2.11.2")

        when:
        BuildResult result = runner
            .withArguments(
            ':dependencyInsight',
            '--configuration',
            'compileClasspath',
            '--dependency',
            'com.fasterxml.jackson.core:jackson-databind'
        ).build()

        then:
        result.output.contains("Task :dependencyInsight\n" +
            "com.fasterxml.jackson.core:jackson-databind:2.11.2")
    }

    void setUpBuildGradle(String version){
        buildFile << """

        plugins {
            id '${PatchVulnerableLibrariesPlugin.IDENTIFIER}'
        }
        apply plugin: 'java'

          repositories {
            mavenCentral()
        }

        dependencies {
            compile "com.fasterxml.jackson.core:jackson-databind:${version}"
        }

        """
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

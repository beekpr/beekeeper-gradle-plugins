package io.beekeeper.gradle.security
import io.beekeeper.gradle.security.PatchVulnerableLibrariesPlugin
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class CycloneDXSpecification extends Specification {

    @Rule
    TemporaryFolder dir
    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(dir.root)
            .withPluginClasspath()
    }

    def "it should be able to generate the SBOM"() {
        given:
        setUpBuildGradle()

        when:
        BuildResult result = runner.withArguments('cyclonedxBom').build()

        then:
        result.output.contains("cyclonedxBom")
    }

    void setUpBuildGradle(){
        buildFile << """

        plugins {
            id '${BeekeeperCycloneDxPlugin.IDENTIFIER}'
        }
        apply plugin: 'java'

          repositories {
            mavenCentral()
        }

        dependencies {
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

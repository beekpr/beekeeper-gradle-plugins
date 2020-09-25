package io.beekeeper.gradle.security
import io.beekeeper.gradle.security.SecurityPlugin
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class SpotbugIsSkippedSpecification extends Specification {

    @Rule
    TemporaryFolder dir
    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(dir.root)
            .withPluginClasspath()
    }

    def "it should not report spotbug vulnerability"() {
        given:
        setUpBuildGradle(true)

        when:
        BuildResult result = runner.withArguments('dependencyCheckAnalyze').build()

        then:
        result.output.contains("Found 0 vulnerabilities")
    }

    def "it should report spotbug vulnerability with no supression"() {
        //NOTE: this test may start failing once spotbugs has no vulnerabilities
        given:
        setUpBuildGradle(false)

        when:
        BuildResult result = runner.withArguments('dependencyCheckAnalyze').build()

        then:
        !result.output.contains("Found 0 vulnerabilities")
    }

    void setUpBuildGradle(boolean useCommonSuppression){
        buildFile << """


        plugins {
            id '${SecurityPlugin.IDENTIFIER}'
            id "com.github.spotbugs" version "2.0.0"

        }
        apply plugin: 'java'

          repositories {
            mavenCentral()
        }

        beekeeperSecurityExtension{
            applyCommonSuppressions ${useCommonSuppression}
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

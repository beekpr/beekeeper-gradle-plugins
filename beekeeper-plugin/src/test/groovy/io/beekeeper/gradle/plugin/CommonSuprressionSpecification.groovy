package io.beekeeper.gradle.plugin

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class CommonSuprressionSpecification extends Specification {

    @Rule
    TemporaryFolder dir
    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(dir.root)
            .withPluginClasspath()
    }

    def "it should not report suppressed vulnerability"() {
        given:
        setUpBuildGradle(true)

        when:
        BuildResult result = runner.withArguments('dependencyCheckAnalyze').build()

        then:
        result.output.contains("Found 0 vulnerabilities")
    }

    def "it should report suppressed vulnerability when not using common suppression"() {
        given:
        setUpBuildGradle(false)

        when:
        BuildResult result = runner.withArguments('dependencyCheckAnalyze').build()

        then:
        result.output.contains("Found 1 vulnerabilities")
    }

    void setUpBuildGradle(boolean useCommonSuppression){
        buildFile << """

   
        plugins {
            id '${BeekeeperPlugin.IDENTIFIER}'
        }
        apply plugin: 'java'
        
          repositories {
            mavenCentral()
        }
        
        dependencies {
            compile 'com.rabbitmq:amqp-client:5.7.3'
        }
        
        BeekeeperSecurityExtension{
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

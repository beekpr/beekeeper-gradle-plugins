package io.beekeeper.gradle.plugin

import io.beekeeper.gradle.security.SecurityPlugin
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

    def "it should work nicely along with clean task"() {
        given:
        setUpBuildGradle(true)

        when:
        BuildResult result = runner.withArguments('clean', 'dependencyCheckAnalyze').build()

        then:
        result.output.contains("Found 0 vulnerabilities")
    }

    void setUpBuildGradle(boolean useCommonSuppression){
        buildFile << """


        plugins {
            id '${SecurityPlugin.IDENTIFIER}'
        }
        apply plugin: 'java'

          repositories {
            mavenCentral()
        }

        dependencies {
            compile "com.rabbitmq:amqp-client:5.7.3"
            compile "org.liquibase:liquibase-groovy-dsl:2.1.0"
            compile "org.codehaus.groovy:groovy-sql:2.4.21"
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

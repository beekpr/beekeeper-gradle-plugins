import io.beekeeper.gradle.security.SecurityPlugin
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class SuppressionSpecification extends Specification {

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
        result.output.contains("appendSuppressionsquarkus")
    }

    def "it should report suppressed vulnerability when not using common suppression"() {
        given:
        setUpBuildGradle(false)

        when:
        BuildResult result = runner.withArguments('dependencyCheckAnalyze').build()

        then:
        !result.output.contains("Found 0 vulnerabilities")
    }

    def "it should not suppress Quarkus specific vulnerabilities when no dependency is present"() {
        given:
        setUpBuildGradleNoQuarkus(true)

        when:
        BuildResult result = runner.withArguments('dependencyCheckAnalyze').build()

        then:
        result.output.contains("Found 0 vulnerabilities")
        !result.output.contains("appendSuppressionsquarkus")
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
            compile "org.codehaus.groovy:groovy-sql:2.4.12"
            compile "io.quarkus:quarkus-resteasy"
            implementation "io.quarkus:quarkus-kubernetes"
        }

        beekeeperSecurityExtension{
            applyCommonSuppressions ${useCommonSuppression}
        }



        """
    }

    void setUpBuildGradleNoQuarkus(boolean useCommonSuppression){
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
            compile "org.codehaus.groovy:groovy-sql:2.4.12"
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

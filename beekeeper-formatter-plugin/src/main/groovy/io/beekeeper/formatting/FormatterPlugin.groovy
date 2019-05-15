package io.beekeeper.formatting

import com.diffplug.common.io.Resources
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class FormatterPlugin implements Plugin<Project> {
	static final String IDENTIFIER = 'io.beekeeper.formatting'
	static final private GradleVersion MIN_GRADLE_VERSION_SUPPORTED = GradleVersion.version("4.6")

	private static final String FORMATTING_CONF_FOLDER = "beekeeper-formatter-rules"
	private static final String JAVA_FORMATTING_RULES_RELATIVE_PATH = "${FORMATTING_CONF_FOLDER}/java/BeekeeperCodeFormat.xml"


	@Override
	void apply(Project project) {
		applyConfigToProject(project)
	}

	void applyConfigToProject(Project project) {
		mandatoryGradleVersionCheck(project)
		project.pluginManager.apply(SpotlessPlugin.class)
		applySpotlessConfiguration(project)
		applyFormatTask(project)
	}

	void applySpotlessConfiguration(Project project) {
		project.spotless {
			java {
				removeUnusedImports()
				trimTrailingWhitespace()
				eclipse().configFile getJavaFormattingRulesAbsoluteProjectPath(project)
			}
		}
	}

	void applyFormatTask(Project project) {
		// Must run afterEvaluate for the spotless tasks to exist.
		project.afterEvaluate { Project pr ->
			// Spotless is not able to load files from a jar, so we extract the configurations in the build folder
			pr.task('extractBeekeeperFormattingConfig') {
				//TODO: copy the whole FORMATTING_CONF_FOLDER
				doLast {
					project.logger.info("Copying formatter definitions to build dir...")
					def source = Resources.asByteSource(Resources.getResource(JAVA_FORMATTING_RULES_RELATIVE_PATH))
					def destination = project.file(getJavaFormattingRulesAbsoluteProjectPath(project))
					destination.getParentFile().mkdirs()
					Files.copy(source.openStream(), Paths.get(destination.toURI()), StandardCopyOption.REPLACE_EXISTING)
					project.logger.info("Formatter definitions successfully copied.")
				}
			}

			// Make the spotless tasks dependent on the config extraction task, so they can reach the config files
			pr.getTasksByName("spotlessCheck", true).first().dependsOn('extractBeekeeperFormattingConfig')
			pr.getTasksByName("spotlessApply", true).first().dependsOn('extractBeekeeperFormattingConfig')
		}
	}

	void mandatoryGradleVersionCheck(Project project) {
		GradleVersion currentGradleVersion = GradleVersion.version(project.gradle.gradleVersion)

		if (currentGradleVersion.compareTo(MIN_GRADLE_VERSION_SUPPORTED) < 0) {
			throw new GradleException("This version of the plugin is incompatible with gradle < 4.6!")
		}
	}

	static String getJavaFormattingRulesAbsoluteProjectPath(Project project) {
		return "${project.buildDir}/${JAVA_FORMATTING_RULES_RELATIVE_PATH}"
	}
}

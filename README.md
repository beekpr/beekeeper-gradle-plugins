# Beekeeper Gradle Plugins

Beekeeper Gradle plugins which come pre-configured to work in the beekeeper environment.
These plugins are mostly thin wrappers around existing plugins and pre-configure the
plugins in a common way for all projects.


## The Plugins

The plugins in this folder are separated into 2 categories: Main plugins and Module Plugins.

The main plugins are the plugins that are meant to be listed in a build.gradle file, whereas
the module plugins are meant to encapsulate a given domain of the build process, such as:

   * Formatting
   * Code Analysis
   * License Checks
   * Security
   * IDE Support
   * Etc...

Currently, there is only one main plugin (`io.beekeeper.gradle.plugin`) and its job
is to apply all the module plugins.

### Main Plugin: `io.beekeeper.gradle.plugin`

The most important plugin. This plugin applies all the plugins in this repository and is
usually the only plugin that needs to be specified in the build gradle files. All other plugins
will be pulled in automatically and applied correctly.

This plugin comes with an additional task called `beekeeperPluginCheck` that verifies
that the plugin is at the correct version. This plugin does nothing more but checks for the currently
required minimum version versus the plugin version. That task is required mostly for automation, so that
PRs can be rejected based on an outdated plugin.

### Module Plugin: `io.beekeeper.gradle.plugins.formatter`

Applies the spotless formatter and configures it with Beekeeper conventions. Currently, supports the following:

   * Java Projects
   * Groovy Projects
   * Build Gradle files

### Module Plugin: `io.beekeeper.gradle.plugins.ide`

Provides IDE support. Applies both `eclipse` and `idea` gradle base plugins.

### Module Plugin: `io.beekeeper.gradle.plugins.license-check`

Provides support for checking software licenses. Applies the `com.github.jk1:gradle-license-report` plugin.
Comes also with a preconfigured list of allowed licenses for Beekeeper

### Module Plugin: `io.beekeeper.gradle.plugins.security`

Provides support for checking for dependencies with known vulnarabilities.
Applies the `org.owasp:dependency-check-gradle` plugin.

### Module Plugin: `io.beekeeper.gradle.plugins.testing`

Provides support for test related tasks..
Applies the `jacoco` plugin.


## Instructions

Follow the [Gradle Plugin Portal installation instructions](https://plugins.gradle.org/plugin/io.beekeeper.gradle.plugin). In general,
you will only need to declare this plugin in the root project. This plugin automatically applies all the plugins into
the child projects.

### Migration Instructions
If you are migrating an existing beekeeper project to make use of this plugin, then perform the following:

   * Apply the plugin as in the instructions
   * Remove any of the following plugins which may be already in use and any of their configuration
      * eclipse
      * jacoco (remove jacoco configuration from gradle)
      * idea
   * That's it!

## Testing locally

Please make sure you're using java 11:
```
$ java -version
```

To test locally you should add to build.gradle
```groovy
id "maven-publish"
if(project.name.endsWith('-plugin')) {
     apply plugin: 'maven-publish'
}
repositories {
     mavenLocal()
}
```
After that you should run `./gradlew publishToMavenLocal` this will deploy plugin locally. After that you can add it to your project with `mavenLocal()`
## Disclaimer

**The repository is public** and is not meant for containing _credentials or other sensitive_ information.

## Changelog

## 0.16.1
* Bump cycloneDX plugin to `2.1.0`

## 0.16.0
Move `beekeeper-formatter-plugin` from Google format to [Palantir format](https://github.com/palantir/palantir-java-format) as voted in the Backend Guild.

### Migration
#### Migration in the project
* Upgrade the plugin version to `0.16.0` or higher.
* Run `./gradlew spotlessApply` to apply the new formatting rules.
* Commit the changes with the message `BG-50: Migrate to Palantir Java Format`.
* Add the commit hash to `.git-blame-ignore-revs` in the project root. Create the file if it doesn't exist.

#### Migration in your IDE
* Install the IntelliJ plugin as described [here](https://github.com/palantir/palantir-java-format?tab=readme-ov-file#intellij-plugin)

### Avoiding the migration
If you want to avoid the migration and continue to use the Google code format, you can add the following configuration to your `build.gradle`:
```groovy
beekeeperCodeFormat {
     useGoogleJavaFormat = true
}
```

## 0.15.0
Apply `quarkus-jacoco` dependency to Quarkus-based projects and configure it to build consolidated reports from both
`@QuarkusTest`-annotated and unit tests.

**Upgrading to this version requires changing how Beekeeper BOM is configured in project's dependencies:**
- Go to you project's service's `build.gradle` and navigate to the `dependencies` section
- Find these three lines:

```groovy
dependencies {
    implementation platform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}")
    universalBom platform("${beekeeperQuarkusPlatformGroupId}:${beekeeperQuarkusPlatformArtifactId}:${beekeeperQuarkusPlatformVersion}")

    // (...)
}
```
- Replace the `platform` method call with `enforcedPlatform`, as follows:

```groovy
dependencies {
    implementation enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}")
    universalBom enforcedPlatform("${beekeeperQuarkusPlatformGroupId}:${beekeeperQuarkusPlatformArtifactId}:${beekeeperQuarkusPlatformVersion}")

    // (...)
}
```

## 0.14.0
Update gradle to 7.6 and build to java 11

Bump security plugin dependencies:
- `dependency-check-gradle` to 8.2.1
- `cyclonedx-gradle-plugin` to 1.7.4

### 0.13.1
Bump `spotless-plugin-gradle` to 6.11.0 to include newer version of `google-java-format`.

### 0.13.0
Bump security plugin dependencies:
- dependency-check-gradle to 7.2.1
- cyclonedx-gradle-plugin to 1.7.2

Migration:
See release notes [here](https://github.com/beekpr/beekeeper-gradle-plugins/releases/tag/0.13.0)

### 0.12.0
Use google-java-format as a default formatter.

### 0.11.x
`beekeeper-code-analysis-plugin` is removed (so does spotbugs)
When upgrading to this version, please remove this line from `lombok.config`:
```
lombok.extern.findbugs.addSuppressFBWarnings = true
```

## License
> Copyright 2019 Beekeeper
>
>Licensed under the Apache License, Version 2.0 (the "License");
>you may not use this file except in compliance with the License.
>You may obtain a copy of the License at
>
>   http://www.apache.org/licenses/LICENSE-2.0
>
>Unless required by applicable law or agreed to in writing, software
>distributed under the License is distributed on an "AS IS" BASIS,
>WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
>See the License for the specific language governing permissions and
>limitations under the License.

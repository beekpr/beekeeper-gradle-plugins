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

### Module Plugin: `io.beekeeper.gradle.plugins.code-analysis-check`

Provides support for looking for common bugs in code. Applies the `gradle.plugin.com.github.spotbugs:spotbugs-gradle-plugin` plugin.
Note, the Java 11 support is still "spotty"

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
      * findbugs (check for tasks.withType(FindBugs))
      * eclipse
      * jacoco (remove jacoco configuration from gradle)
      * idea
   * That's it!

## Disclaimer

**The repository is public** and is not meant for containing _credentials or other sensitive_ information.

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

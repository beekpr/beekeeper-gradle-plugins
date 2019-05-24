# Beekeeper Formatter Plugin

This is a gradle Plugin that helps to keep the code of your project clean and well formatted.

This plugin makes use of the **spotless plugin** and include a default spotless configuration which reflects the formatting guidelines adopted in Beekeeper.

**Currently supported languages**
* Java



## What the plugin can do

Given that the plugin simply wraps the spotless plugin pre-configured, it offers the same functionalities of spotless:
* The formatting command is IDE agnostic and is runnable as a gradle task
* It's possible to check the validity of the current project's code formatting without applying any changes.
* The validity of the current formatting is **automatically checked on build**
* The formatting task can be finely [applied to specific target files](https://github.com/diffplug/spotless/tree/master/plugin-gradle#can-i-apply-spotless-to-specific-files)


## How to use it

Import the plugin in your gradle project by adding this line to the `build.gradle` file:
```grovy
plugins {
    // ...other plugins defined
    id 'io.beekeeper.formatter' version '0.2.0'
}
```

Run one of these gradle tasks:
* `spotlessCheck`: Check the formatting of your project's code
* `spotlessApply`: Apply Beekeeper's formatting rules to the project's code.

Additional usage suggestions:
* Fix formatting of your code on `git commit`.
> Edit `.git/hooks/pre-commit` (or create it) to call the spotlessApply gradle task
* Append or prepend the formatting task to other crucial gradle tasks of your project.
* Set a shortcut in your IDE or editor for running the formatting task.



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

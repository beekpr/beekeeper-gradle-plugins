package io.beekeeper.gradle.testing

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class SpecificationWithBuildFiles extends Specification {

    @Rule
    TemporaryFolder dir

    File getSettingsFile() {
        file('settings.gradle')
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
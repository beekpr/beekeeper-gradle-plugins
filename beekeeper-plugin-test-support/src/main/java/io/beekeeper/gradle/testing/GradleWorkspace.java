package io.beekeeper.gradle.testing;

import java.io.File;
import java.io.IOException;

import org.gradle.testkit.runner.GradleRunner;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

public class GradleWorkspace extends ExternalResource {

    private final TempRule projectFolder = new TempRule();

    @Override
    protected void before() throws Throwable {
        super.before();
        projectFolder.before();
    }

    @Override
    protected void after() {
        super.after();
        projectFolder.after();
    }

    public File getBuildFile() {
        return getFile("build.gradle");
    }

    public File getSettingsFile() {
        return getFile("settings.gradle");
    }

    public File file(String path) {
        return getFile(path);
    }

    public File getFile(String path) {
        try {
            File f = new File(projectFolder.getRoot(), path);
            if (!f.exists()) {
                IGNORE_RESULT(f.getParentFile().mkdirs());
                return projectFolder.newFile(path);
            }
            return f;
        } catch (IOException e) {
            throw new RuntimeException("Could not set up required project files");
        }
    }

    public GradleRunner getRunner() {
        return GradleRunner.create().withProjectDir(projectFolder.getRoot());
    }

    private static class TempRule extends TemporaryFolder {
        @Override
        protected void before() throws Throwable {
            super.before();
        }

        @Override
        protected void after() {
            super.after();
        }
    }

    @SuppressWarnings("unused")
    private static void IGNORE_RESULT(boolean b) {
    }

}

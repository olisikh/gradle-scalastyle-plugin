package org.github.alisiikh.scalastyle

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

abstract class ScalastyleFunSpec extends Specification {

    static class Projects {
        static SINGLE_MODULE = 'single-module'
        static SINGLE_MODULE_MULTI_SOURCE = 'single-module-multi-source'
        static TRAILING_COMMA = 'trailing-comma'
    }

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder()

    @Shared
    private File testProjectBuildDir

    abstract String getProjectName()

    String getScalastyleConfig() {
        ""
    }

    def setup() {
        FileUtils.copyDirectory(new File(this.class.getResource("/$projectName").file), testProjectDir.root)
        testProjectBuildDir = new File(testProjectDir.root, 'build')

        generateBuildGradleFile(scalastyleConfig)
    }

    def cleanup() {
        testProjectBuildDir.deleteDir()
    }

    def generateBuildGradleFile(String scalastyleConfig = "") {
        testProjectDir.newFile("build.gradle")
                .write("""
plugins {
    id 'com.github.alisiikh.scalastyle'
}

repositories {
    jcenter()
}

dependencies {
    compile 'org.scala-lang:scala-library:2.12.8'
}

${scalastyleConfig}
""")
    }

    File getProjectDir() { testProjectDir.root }
    File getBuildDir() { testProjectBuildDir }
    File getReportFor(String name) {
        new File(testProjectBuildDir, "/scalastyle/$name/scalastyle-check.xml")
    }

    GradleRunner createRunner(String task) {
        GradleRunner.create()
                .withPluginClasspath()
                .forwardOutput()
                .withProjectDir(testProjectDir.root)
                .withArguments(task)
    }

    BuildResult runGradleAndSucceed(String task) {
        createRunner(task).build()
    }

    BuildResult runGradleAndFail(String task) {
        createRunner(task).buildAndFail()
    }

    BuildResult runGradleAndSucceed(String task, String gradleVersion) {
        createRunner(task)
                .withGradleVersion(gradleVersion)
                .build()
    }
}

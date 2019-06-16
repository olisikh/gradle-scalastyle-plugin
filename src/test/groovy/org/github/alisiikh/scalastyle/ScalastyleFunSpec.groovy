package org.github.alisiikh.scalastyle

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

abstract class ScalastyleFunSpec extends Specification {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder()

    @Shared
    File testProjectBuildDir

    def createBuildFolder(String projectDir) {
        FileUtils.copyDirectory(new File(this.class.getResource("/$projectDir").file), testProjectDir.root)
        testProjectBuildDir = new File(testProjectDir.root, "build")
    }

    def generateBuildGradleFile(String scalastyleConfig = "") {
        testProjectDir.newFile("build.gradle").write("""
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

    File prepareTest(String projectDir, String scalastyleOverrides = null) {
        createBuildFolder(projectDir)
        generateBuildGradleFile(scalastyleOverrides)
    }

    GradleRunner createRunner(String task) {
        GradleRunner.create()
                .withPluginClasspath()
                .forwardOutput()
                .withProjectDir(testProjectDir.root)
                .withDebug(true)
                .withArguments('--stacktrace', '--info', task)
    }

    BuildResult executeGradle(String task) {
        createRunner(task).build()
    }

    BuildResult executeGradleAndFail(String task) {
        createRunner(task).buildAndFail()
    }

    BuildResult executeGradle(String task, String gradleVersion) {
        createRunner(task)
                .withGradleVersion(gradleVersion)
                .build()
    }
}

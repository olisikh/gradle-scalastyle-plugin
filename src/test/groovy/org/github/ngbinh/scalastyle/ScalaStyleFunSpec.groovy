package org.github.ngbinh.scalastyle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Shared

abstract class ScalaStyleFunSpec extends ScalaStyleSpec {

    @Shared
    List<File> pluginClasspath

    def setupSpec() {
        def current = getClass().getResource("/").file
        pluginClasspath = [
                current.replace("classes/test", "classes/main"),
                current.replace("classes/test", "resources/main")
        ].collect { new File(it) }
    }

    File prepareTest(String projectDir, String scalaStyleOverrides = null) {
        createBuildFolder(projectDir)
        generateBuildGradleFile(scalaStyleOverrides)
    }

    GradleRunner createRunner(String task) {
        GradleRunner.create()
                .forwardOutput()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath(pluginClasspath)
                .withDebug(true)
                .withArguments('--stacktrace', task)
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

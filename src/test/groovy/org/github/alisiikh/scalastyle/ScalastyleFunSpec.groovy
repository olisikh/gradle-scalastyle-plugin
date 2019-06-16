package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner

abstract class ScalastyleFunSpec extends ScalastyleSpec {

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

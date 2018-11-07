package org.github.ngbinh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.NO_SOURCE
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ScalaStyleCheckRunsOnCheckSpec extends ScalaStyleFunSpec {

    def "should run scalaStyleCheck when Gradle's check task is invoked"() {
        setup:
        prepareTest("simple")

        when:
        BuildResult result = executeGradle('check')

        then:
        result.task(":scalaStyleCheck").outcome == SUCCESS
        result.task(':scalaStyleMainCheck').outcome == SUCCESS
        result.task(':scalaStyleTestCheck').outcome == NO_SOURCE
    }
}

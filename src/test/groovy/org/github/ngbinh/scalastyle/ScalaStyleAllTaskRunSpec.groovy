package org.github.ngbinh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.NO_SOURCE
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ScalaStyleAllTaskRunSpec extends ScalaStyleFunSpec {

    def "should run scalaStyleAll"() {
        setup:
        prepareTest("zinc")

        when:
        BuildResult result = executeGradle('scalaStyleAll')

        then: "compiled successfully"
        result.task(":scalaStyleAll").outcome == SUCCESS
        result.task(':scalaStyleMain').outcome == SUCCESS
        result.task(':scalaStyleTest').outcome == NO_SOURCE
    }
}

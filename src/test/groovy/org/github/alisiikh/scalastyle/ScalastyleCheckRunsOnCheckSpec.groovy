package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.NO_SOURCE
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ScalastyleCheckRunsOnCheckSpec extends ScalastyleFunSpec {

    def "should run scalastyleCheck when Gradle's check task is invoked"() {
        setup:
        prepareTest("single-module")

        when:
        BuildResult result = executeGradle('check')

        then:
        result.task(":scalastyleCheck").outcome == SUCCESS
        result.task(':scalastyleMainCheck').outcome == SUCCESS
        result.task(':scalastyleTestCheck').outcome == NO_SOURCE
    }
}

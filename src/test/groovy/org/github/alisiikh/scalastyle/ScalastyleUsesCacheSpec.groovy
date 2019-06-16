package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.*

class ScalastyleUsesCacheSpec extends ScalastyleFunSpec {

    def "should run scalastyleCheck"() {
        setup:
        prepareTest("single-module")

        when:
        BuildResult result = executeGradle('scalastyleCheck')

        then: "compiled successfully"
        result.task(":scalastyleCheck").outcome == SUCCESS
        result.task(':scalastyleMainCheck').outcome == SUCCESS
        result.task(':scalastyleTestCheck').outcome == NO_SOURCE

        when: "run again without clean"
        result = executeGradle('scalastyleCheck')

        then: "scalastyleCheck should not be run"
        result.task(":scalastyleCheck").outcome == UP_TO_DATE
        result.task(':scalastyleMainCheck').outcome == UP_TO_DATE
        result.task(':scalastyleTestCheck').outcome == NO_SOURCE
    }
}

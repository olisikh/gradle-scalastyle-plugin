package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.NO_SOURCE
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ScalastyleCheckRunsDuringCheckSpec extends ScalastyleFunSpec {

    @Override
    String getProjectName() { Projects.SINGLE_MODULE }

    def "should run scalastyleCheck when running check"() {
        when:
        BuildResult result = runGradleAndSucceed('check')

        then:
        result.task(":scalastyleCheck").outcome == SUCCESS
        result.task(':scalastyleMainCheck').outcome == SUCCESS
        result.task(':scalastyleTestCheck').outcome == NO_SOURCE
    }
}

package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.NO_SOURCE
import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE

class ScalastyleCheckIsCacheableSpec extends ScalastyleFunSpec {

    @Override
    String getProjectName() { Projects.SINGLE_MODULE }

    def "should run scalastyleCheck"() {
        setup: "scalastyleCheck is already executed and successful"
        runGradleAndSucceed('scalastyleCheck')

        when:
        BuildResult result = runGradleAndSucceed('scalastyleCheck')

        then:
        result.task(":scalastyleCheck").outcome == UP_TO_DATE
        result.task(':scalastyleMainCheck').outcome == UP_TO_DATE
        result.task(':scalastyleTestCheck').outcome == NO_SOURCE
    }
}

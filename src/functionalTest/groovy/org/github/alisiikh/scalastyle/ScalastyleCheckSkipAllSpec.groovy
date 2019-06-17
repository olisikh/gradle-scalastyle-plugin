package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.SKIPPED
import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE

class ScalastyleCheckSkipAllSpec extends ScalastyleFunSpec {

    @Override
    String getProjectName() { Projects.SINGLE_MODULE_MULTI_SOURCE }
    String getScalastyleConfig() {
        """
scalastyle {
  skip = true
}
"""
    }

    def "should skip all the source sets if skip is set to true globally"() {
        when:
        BuildResult result = runGradleAndSucceed('scalastyleCheck')

        then:
        result.task(":scalastyleCheck").outcome == UP_TO_DATE
        result.task(':scalastyleMainCheck').outcome == SKIPPED
        result.task(':scalastyleTestCheck').outcome == SKIPPED
    }
}

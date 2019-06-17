package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.SKIPPED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ScalastyleCheckSkipSourceSetSpec extends ScalastyleFunSpec {

    @Override
    String getProjectName() { Projects.SINGLE_MODULE_MULTI_SOURCE }
    String getScalastyleConfig() {
        """
scalastyle {
  sourceSets {
    main {
      skip = true
    }
  }
}
"""
    }

    def "should skip the source set if skip is set to true"() {
        when:
        BuildResult result = runGradleAndSucceed('scalastyleCheck')

        then:
        result.task(":scalastyleCheck").outcome == SUCCESS
        result.task(':scalastyleMainCheck').outcome == SKIPPED
        result.task(':scalastyleTestCheck').outcome == SUCCESS
    }
}

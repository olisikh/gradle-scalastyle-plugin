package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.FAILED

class ScalastyleCheckFailOnWarningSpec extends ScalastyleFunSpec {

    @Override
    String getProjectName() { Projects.SINGLE_MODULE }
    @Override
    String getScalastyleConfig() {
        """
scalastyle {
    failOnWarning = true
}
"""
    }

    def "should fail scalastyleCheck if failOnWarning flag is enabled and a warning rule is violated"() {
        when:
        BuildResult result = runGradleAndFail('scalastyleCheck')

        then: "build fails because of a scalastyle warning"
        result.task(':scalastyleMainCheck').outcome == FAILED
        result.output.contains """
Processed 1 file(s)
Found 0 errors
Found 1 warnings"""
    }
}

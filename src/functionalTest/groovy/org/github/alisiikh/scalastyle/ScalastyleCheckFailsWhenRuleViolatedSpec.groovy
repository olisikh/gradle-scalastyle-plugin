package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.FAILED

class ScalastyleCheckFailsWhenRuleViolatedSpec  extends ScalastyleFunSpec {

    @Override
    String getProjectName() { Projects.SINGLE_MODULE }
    @Override
    String getScalastyleConfig() {
        """
scalastyle {
  config = file("\$rootDir/scalastyle_strict.xml")
}
"""
    }

    def "should fail build if scalastyle error rule is violated"() {
        when:
        BuildResult result = runGradleAndFail('scalastyleCheck')

        then: "build fails because of a scalastyle error"
        result.task(':scalastyleMainCheck').outcome == FAILED
        result.output.contains """
Processed 1 file(s)
Found 1 errors
Found 0 warnings"""
    }
}
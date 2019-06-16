package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.NO_SOURCE

class ScalastyleCheckCoubleBeRunForSpecificSourceSetSpec extends ScalastyleFunSpec {

    @Override
    String getProjectName() { Projects.SINGLE_MODULE }

    def "should run scalastyleTestCheck only"() {
        when:
        BuildResult result = runGradleAndSucceed('scalastyleTestCheck')

        then:
        result.task(':scalastyleTestCheck').outcome == NO_SOURCE
        result.task(':scalastyleMainCheck') == null
        result.task(':scalastyleCheck') == null
    }
}

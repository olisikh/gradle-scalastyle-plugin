package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.NO_SOURCE
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ScalaStyleParsesTrailingCommas extends ScalastyleFunSpec {
    @Override
    String getProjectName() { Projects.TRAILING_COMMA }

    def "should correctly parse projects with trailing comma sources"() {
        when:
        BuildResult result = runGradleAndSucceed('scalastyleCheck')

        then: "compiled successfully"
        result.task(":scalastyleCheck").outcome == SUCCESS
        result.task(':scalastyleMainCheck').outcome == SUCCESS
        result.task(':scalastyleTestCheck').outcome == NO_SOURCE
    }
}

package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ScalastyleCheckRunsOnAllScalaSourcesSpec extends ScalastyleFunSpec {

    String getProjectName() { Projects.SINGLE_MODULE_MULTI_SOURCE }
    String getScalastyleConfig() {
        """
sourceSets {
    intTest {
        scala {
            compileClasspath += main.output
            runtimeClasspath += main.output
        }
    }
}

configurations {
    intTestCompile.extendsFrom compile
    intTestRuntime.extendsFrom compile
}

scalastyle {
    // global config, used in case not overriden specifically
    config = file("\$rootDir/scalastyle_config.xml")
    verbose = false
}
"""
    }

    def "should run scalastyle for all known known scala sources"() {
        when:
        BuildResult result = runGradleAndSucceed('scalastyleCheck')

        then: "all scalastyle check tasks for all scala sources are executed"
        result.task(':scalastyleMainCheck').outcome == SUCCESS
        result.task(":scalastyleTestCheck").outcome == SUCCESS
        result.task(":scalastyleIntTestCheck").outcome == SUCCESS
        result.task(":scalastyleCheck").outcome == SUCCESS
        result.output.contains """
Processed 1 file(s)
Found 0 errors
Found 0 warnings
"""

        then: "reports are created"
        def mainReport = getReportFor('main')
        def testReport = getReportFor('test')
        def intTestReport = getReportFor('intTest')

        mainReport.exists() && mainReport.isFile()
        testReport.exists() && testReport.isFile()
        intTestReport.exists() && intTestReport.isFile()

    }
}

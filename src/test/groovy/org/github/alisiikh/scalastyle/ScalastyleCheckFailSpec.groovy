package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

class ScalastyleCheckFailSpec extends ScalastyleFunSpec {

    def "should run scalastyleCheck and fail on warning"() {
        setup:
        prepareTest("simple", """
scalastyle {
    config = file("\$rootDir/scalastyle_config.xml")
    verbose = false
    failOnWarning = true
}
""")

        when:
        BuildResult result = executeGradleAndFail('scalastyleCheck')

        then: "task scalastyleCheck fails because of a warning"
        result.output.contains """Processed 1 file(s)
Found 0 errors
Found 1 warnings"""
    }
}

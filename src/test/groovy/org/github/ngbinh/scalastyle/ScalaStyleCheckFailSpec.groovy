package org.github.ngbinh.scalastyle

import org.gradle.testkit.runner.BuildResult

class ScalaStyleCheckFailSpec extends ScalaStyleFunSpec {

    def "should run scalaStyleCheck and fail on warning"() {
        setup:
        prepareTest("simple", """
scalaStyle {
    config = file("\$rootDir/scalastyle.xml")
    verbose = false
    failOnWarning = true
}
""")

        when:
        BuildResult result = executeGradleAndFail('scalaStyleCheck')

        then: "task scalaStyleCheck fails because of a warning"
        result.output.contains "You have 1 Scalastyle violation(s)."
    }
}

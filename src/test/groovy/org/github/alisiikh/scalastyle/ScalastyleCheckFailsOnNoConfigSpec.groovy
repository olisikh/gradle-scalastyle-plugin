package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

class ScalastyleCheckFailsOnNoConfigSpec extends ScalastyleFunSpec {

    def "should run scalastyleCheck and fail if no scalastyle.xml config provided"() {
        setup:
        prepareTest("single-module", """
scalastyle {
  config = file("\$rootDir/scalastyle_unknown.xml")
}
""")

        when:
        BuildResult result = executeGradleAndFail('scalastyleCheck')

        then:
        result.output.contains "Scalastyle configuration file does not exist"
    }
}

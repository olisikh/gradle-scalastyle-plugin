package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

class ScalastyleCheckFailsWhenNoConfigSpec extends ScalastyleFunSpec {

    @Override
    String getProjectName() { Projects.SINGLE_MODULE }
    @Override
    String getScalastyleConfig() {
        """
scalastyle {
  config = file("\$rootDir/scalastyle_unknown.xml")
}
"""
    }

    def "should fail during project configuration if no scalastyle config provided"() {
        when:
        BuildResult result = runGradleAndFail('scalastyleCheck')

        then:
        result.output.contains "Scalastyle configuration file does not exist"
    }
}

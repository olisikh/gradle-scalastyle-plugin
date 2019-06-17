package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ScalastyleCheckOutputPathOverriddenSpec extends ScalastyleFunSpec {

    @Override
    String getProjectName() { Projects.SINGLE_MODULE }
    String getScalastyleConfig() {
        """
scalastyle {
  sourceSets {
    main {
      output = file("\$rootDir/scalastyle-report.xml")
    }
  }
}
"""
    }

    def "should run scalastyleCheck and output report to custom location"() {
        when:
        BuildResult result = runGradleAndSucceed('scalastyleMainCheck')

        then:
        result.task(':scalastyleMainCheck').outcome == SUCCESS

        and: "report is located at custom path"
        def reportFile = new File(projectDir, "scalastyle-report.xml")
        reportFile.text.contains("""<error line="1" source="org.scalastyle.file.HeaderMatchesChecker" severity="warning" message="Header does not match expected text"/>""")
    }
}

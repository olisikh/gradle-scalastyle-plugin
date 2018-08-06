import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.NO_SOURCE
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ScalaStyleAllTaskRunSpec extends ScalaStyleFunSpec {

    String scalaVersion = System.properties['SCALA_VERSION']

    def "should run scalaStyleAll"() {
        setup:
        prepareTest(scalaVersion)

        when:
        BuildResult result = executeGradle('scalaStyleAll')

        then: "compiled successfully"
        result.task(":scalaStyleAll").outcome == SUCCESS
        result.task(':scalaStyleMain').outcome == SUCCESS
        result.task(':scalaStyleTest').outcome == NO_SOURCE
    }
}

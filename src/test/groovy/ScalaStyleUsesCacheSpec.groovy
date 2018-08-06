import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.*

class ScalaStyleUsesCacheSpec extends ScalaStyleFunSpec {

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

        when: "run again without clean"
        result = executeGradle('scalaStyleAll')

        then: "scalaStyle should not be run"
        result.task(":scalaStyleAll").outcome == UP_TO_DATE
        result.task(':scalaStyleMain').outcome == UP_TO_DATE
        result.task(':scalaStyleTest').outcome == NO_SOURCE
    }
}

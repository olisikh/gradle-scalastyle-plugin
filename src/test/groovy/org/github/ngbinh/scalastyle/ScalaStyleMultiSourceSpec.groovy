package org.github.ngbinh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.*

class ScalaStyleMultiSourceSpec extends ScalaStyleFunSpec {

    String baseConfig = """
sourceSets {
    garbage {
        scala {
            compileClasspath += main.output
            runtimeClasspath += main.output
        }
    }
}

configurations {
    garbageCompile.extendsFrom compile
    garbageRuntime.extendsFrom compile
}

scalaStyle {
    // global config, used in case not overriden specifically
    config = file("\$rootDir/scalastyle.xml")
    verbose = false
}
"""

    def "should succeed on scalaStyleTest"() {
        setup:
        prepareTest("multi", baseConfig + """
scalaStyle {
    sourceSets {
        test {
            // override output folder
            output = file("\$buildDir/scalastyle-test-result.xml")
        }
    }
}
""")

        when:
        BuildResult result = executeGradle('scalaStyleTest')

        then:
        result.task(":scalaStyleTest").outcome == SUCCESS
        result.output.contains """
> Task :scalaStyleTest
Processed 1 file(s)
Found 0 warnings
Found 0 errors"""

        and: "report is formed in custom location"
        def report = new File(testProjectBuildDir, "scalastyle-test-result.xml").text
        report == """<?xml version="1.0" encoding="UTF-8"?>
<checkstyle version="5.0">
      
    </checkstyle>
"""
    }

    def "should succeed on scalaStyleMain"() {
        setup:
        prepareTest("multi", baseConfig + """
scalaStyle {
    sourceSets {
        main {
            // no overrides
        }
    }
}
""")

        when:
        def result = executeGradle('scalaStyleMain')

        then:
        result.task(':scalaStyleMain').outcome == SUCCESS
        result.output.contains ""
        result.output.contains """> Task :scalaStyleMain
Processed 1 file(s)
Found 0 warnings
Found 0 errors"""
    }

    def "should fail on scalaStyleMain with custom configuration"() {
        setup:
        prepareTest("multi", baseConfig + """
scalaStyle {
    sourceSets {
        main {
            // override config
            config = file("\$rootDir/scalastyle_main.xml")
        }
    }
}
""")

        when:
        def result = executeGradleAndFail('scalaStyleMain')

        then:
        result.task(':scalaStyleMain').outcome == FAILED
        result.output.contains "Task :scalaStyleMain FAILED"
        result.output.contains """Processed 1 file(s)
Found 0 warnings
Found 1 errors"""
        result.output.contains """src/main/scala/Person.scala message=Header does not match expected text line=1"""
    }

    def "should not have a task scalaStyleGarbage"() {
        setup:
        prepareTest("multi", baseConfig + """
scalaStyle {
    sourceSets {
        garbage {
            // do not even create scalaStyleGarbage task for this source set
            skip = true
        }
    }
}
""")

        when:
        def result = executeGradleAndFail('scalaStyleGarbage')

        then:
        result.output.contains "Task 'scalaStyleGarbage' not found in root project"
    }
}

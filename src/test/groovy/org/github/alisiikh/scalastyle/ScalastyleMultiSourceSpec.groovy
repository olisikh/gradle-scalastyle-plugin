package org.github.alisiikh.scalastyle

import org.gradle.testkit.runner.BuildResult

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ScalastyleMultiSourceSpec extends ScalastyleFunSpec {

    private String baseConfig = """
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

    def "should succeed on scalastyleTestCheck"() {
        setup:
        prepareTest("single-module-multi-source", baseConfig + """
scalastyle {
    sourceSets {
        test {
            // override output folder
            output = file("\$buildDir/scalastyle-test-result.xml")
        }
    }
}
""")

        when:
        BuildResult result = executeGradle('scalastyleTestCheck')

        then:
        result.task(":scalastyleTestCheck").outcome == SUCCESS
        result.output.contains """
Processed 1 file(s)
Found 0 errors
Found 0 warnings
"""

        and: "report is formed in custom location"
        def report = new File(testProjectBuildDir, "scalastyle-test-result.xml").text
        report == """<?xml version="1.0" encoding="UTF-8"?>
<checkstyle version="5.0">
      
    </checkstyle>
"""
    }

    def "should succeed on scalastyleMainCheck"() {
        setup:
        prepareTest("single-module-multi-source", baseConfig + """
scalastyle {
    sourceSets {
        main {
            // no overrides
        }
    }
}
""")

        when:
        def result = executeGradle('scalastyleMainCheck')

        then:
        result.task(':scalastyleMainCheck').outcome == SUCCESS
        result.output.contains """
Processed 1 file(s)
Found 0 errors
Found 0 warnings
"""
    }

    def "should fail on scalastyleMainCheck with custom configuration"() {
        setup:
        prepareTest("single-module-multi-source", baseConfig + """
scalastyle {
    sourceSets {
        main {
            // override config
            config = file("\$rootDir/scalastyle_main.xml")
        }
    }
}
""")

        when:
        def result = executeGradleAndFail('scalastyleMain')

        then:
        result.task(':scalastyleMainCheck').outcome == FAILED
        result.output.contains "Task :scalastyleMainCheck FAILED"
        result.output.contains """Processed 1 file(s)
Found 1 errors
Found 0 warnings
"""
        result.output.contains """src/main/scala/Person.scala message=@deprecated should be used instead of @java.lang.Deprecated line=17 column=0"""
    }

    def "should not have a task scalastyleIntTestCheck"() {
        setup:
        prepareTest("single-module-multi-source", baseConfig + """
scalastyle {
    sourceSets {
        intTest {
            // do not even create scalastyleIntTestCheck task for this source set
            skip = true
        }
    }
}
""")

        when:
        def result = executeGradleAndFail('scalastyleIntTestCheck')

        then:
        result.output.contains "Task 'scalastyleIntTestCheck' not found in root project"
    }
}

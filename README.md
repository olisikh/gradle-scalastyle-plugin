# Scalastyle Gradle Plugin

![Build](https://travis-ci.org/alisiikh/gradle-scalastyle-plugin.svg?branch=master)

Gradle plugin for Scalastyle http://www.scalastyle.org/

Originally forked from: https://github.com/ngbinh/gradle-scalastyle-plugin

### Instructions

Please refer to the plugin page on how to install it: https://plugins.gradle.org/plugin/com.github.alisiikh.scalastyle

Configure the plugin:

```groovy
scalastyle {
  scalaVersion = '2.12' // default
  scalastyleVersion = '1.0.0' // default
  config = file("${projectDir}/scalastyle_config.xml") // path to scalastyle config xml file, default: false
  skip = false  // skips scalastyle check if set to true, default: false
  inputEncoding = 'UTF-8' // default
  outputEncoding = 'UTF-8' // default
  failOnWarning = false // default
  verbose = false // default
  quiet = false // default
}
```

Properties that can be specified only per source set (when specified, they are used in favor of global ones):

```groovy
  output = file("${buildDir}/scalastyle/${sourceSet.name}/scalastyle-check.xml")
  config = file("${projectDir}scalastyle_config.xml")
  skip = false
  failOnWarning = false
```

Example configuration for a project with multiple source sets and different scalastyle checking rules:

```groovy
  scalastyle {
    failOnWarning = true
    verbose = false
    quiet = true
  
    // source sets must be defined in the project
    sourceSets {
      main {
        output = file("${projectDir}/scalastyle-main-report.xml") // output the main report to a specific location
      }
      test {
        config = file("${projectDir}/scalastyle-test.xml") // use different config for test
        failOnWarning = false // fail on warning when running scalastyle for main source set
      }
      perfTest {
        skip = true // don't run scalastyle for perfTest source set at all
      }
    }
  }
```

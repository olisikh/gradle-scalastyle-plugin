# Scalastyle Gradle Plugin

![Build](https://travis-ci.org/alisiikh/gradle-scalastyle-plugin.svg?branch=master)

Gradle plugin for Scalastyle (beautiful-scala fork) https://scalastyle.beautiful-scala.com/

Originally forked from: https://github.com/ngbinh/gradle-scalastyle-plugin

### Install

Please refer to the plugin page on how to install it: https://plugins.gradle.org/plugin/com.github.alisiikh.scalastyle


### Configuration
If you have a config scalastyle_config.xml in the root of your project
you don't need to specify configuration for the plugin

Plugin configuration (example contains default values):

```groovy
scalastyle {
  scalaVersion = '2.12'
  scalastyleVersion = '1.5.1'
  config = file("${projectDir}/scalastyle_config.xml") // path to scalastyle config xml file
  skip = false  // skips scalastyle check if set to true
  inputEncoding = 'UTF-8'
  outputEncoding = 'UTF-8'
  failOnWarning = false
  verbose = false
  quiet = false
  forkOptions {} // JavaForkOptions that determine fork options of the daemon worker(s)
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

Example configuration of `forkOptions` parameter:

```groovy
  scalastyle {
    forkOptions {
      maxHeapSize = '64m'
    }
  }
```

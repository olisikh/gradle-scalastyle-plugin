# Scalastyle Gradle Plugin

![Build](https://travis-ci.org/alisiikh/gradle-scalastyle-plugin.svg?branch=master)

Gradle plugin for Scalastyle http://www.scalastyle.org/

Originally forked from: https://github.com/ngbinh/gradle-scalastyle-plugin

### Instructions

https://plugins.gradle.org/plugin/com.github.alisiikh.scalastyle

Use:

```groovy
plugins {
  id "com.github.alisiikh.scalastyle"
}
```

Or via buildScript:
```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.github.alisiikh:gradle-scalastyle-plugin:3.0.0"
  }
}

apply plugin: "com.github.alisiikh.scalastyle"
```

Configure the plugin:

```groovy
scalastyle {
  scalaVersion = '2.12' // default
  scalastyleVersion = '1.0.0' // default
  config = file("${projectDir}/scalastyle.xml") // default
}
```

Global properties that you can override:

```groovy
  inputEncoding = 'UTF-8' // default
  outputEncoding = 'UTF-8' // default
  skip = false  // don't create scalastyle tasks, defaults to false
  failOnWarning = false // fails the build if scalastyle detects a warning rule violation, defaults to false
  verbose = false // sets -v flag of scalastyle, defaults to false
  quiet = false // sets -q flag of scalastyle, defaults to false
```

Properties that can be overridden only per source set:

```groovy
  output = file("${buildDir}/scalastyle/${sourceSet.name}/scalastyle-check.xml") // default
```

You can override properties per source set like this:

```groovy
  scalastyle {
    sourceSets {
      main {
        output = "${projectDir}/scalastyle-main-report.xml" // output the main report to a specific location
      }
      test {
        quiet = true
        config = "${projectDir}/scalastyle-test.xml" // use different config just for test source set
      }
      perfTest {
        skip = true // scalastyle will ignore perfTest source set
      }
    }
  }
```


#### Full Buildscript Example
```groovy
  buildscript {
    repositories {
      jcenter()
      maven { url "https://plugins.gradle.org/m2/" }
    }

    dependencies {
      classpath 'com.github.alisiikh:gradle-scalastyle-plugin:3.0.0'
    }
  }

  apply plugin: 'com.github.alisiikh.scalastyle'

  scalastyle {
    config = file("$rootDir/scalastyle-config.xml")
  }
```

#### Custom configuration per sourceSet
```
  buildscript {
    repositories {
      jcenter()
      maven { url "https://plugins.gradle.org/m2/" }
    }

    dependencies {
      classpath 'com.github.alisiikh:gradle-scalastyle-plugin:3.0.0'
    }
  }

  apply plugin: 'com.github.alisiikh.scalastyle'

  scalastyle {
    config = file("$rootDir/scalastyle-config.xml")

    sourceSets {
      test {
        // specifically configure scalastyle for test sourceSet
        config = file("$rootDir/scalastyle_test.xml")
        failOnWarning = true
      }

      intTest {
        // override output report path only for the intTest sourceSet
        output = file("$projectDir/scalastyle-intTest-check.xml")
      }
    }
  }
```

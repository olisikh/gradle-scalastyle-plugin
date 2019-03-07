# Scala Style Gradle Plugin

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

Configure the plugin

```groovy
scalastyle {
  scalaVersion = '2.12' // default
  scalastyleVersion = '1.0.0' // default
  config = file("${projectDir}/scalastyle.xml")
}
```

Other optional properties are

```groovy
  output = file("${buildDir}/scalastyle/${sourceSet.name}/scalastyle-check.xml") // default
  inputEncoding = 'UTF-8' // default
  outputEncoding = 'UTF-8' // default
  failOnWarning = false // default
  skip = false  // default
  verbose = false // default
  quiet = false // default
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
    config = file("$rootDir/scalastyle_config.xml")

    sourceSets {
      test {
        // specifically configure scalastyle for test sourceSet
        config = file("$rootDir/scalastyle_test.xml")
        failOnWarning = true
      }

      intTest {
        // override output report for intTest sourceSet
        // but still use global scalastyle_config.xml
        output = file("$projectDir/scalastyle-intTest-check.xml")
      }
    }
  }
```

# Scala Style Gradle Plugin

![Build](https://travis-ci.org/alisiikh/gradle-scalastyle-plugin.svg?branch=master)

Gradle plugin for Scalastyle http://www.scalastyle.org/

Originally forked from: https://github.com/ngbinh/gradle-scalastyle-plugin

### Instructions

https://plugins.gradle.org/plugin/com.github.alisiikh.scalastyle_2.12

Use:

```groovy
plugins {
  id "com.github.alisiikh.scalastyle_2.10" version "2.0.2"
}
```
```groovy
plugins {
  id "com.github.alisiikh.scalastyle_2.11" version "2.0.2"
}
```
```groovy
plugins {
  id "com.github.alisiikh.scalastyle_2.12" version "2.0.2"
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
    classpath "com.github.alisiikh:gradle-scalastyle-plugin_2.10:2.0.2" // or 2.11, 2.12
  }
}

apply plugin: "com.github.alisiikh.scalastyle_2.10" // or 2.11, 2.12
```

Configure the plugin

```groovy
scalaStyle {
  config = file("/path/to/scalaStyle.xml")
}
```

Other optional properties are

```groovy
  output //Default => ${buildDir}/scalastyle/${sourceSet.name}/scalastyle-check.xml
  outputEncoding //Default => UTF-8
  failOnViolation //Default => true
  failOnWarning //Default => false
  skip  //Default => false
  verbose //Default => false
  quiet //Default => false
  inputEncoding //Default => UTF-8
```

#### Full Buildscript Example
```groovy
  buildscript {
    repositories {
      jcenter()
      maven { url "https://plugins.gradle.org/m2/" }
    }

    dependencies {
      classpath 'com.github.alisiikh:gradle-scalastyle-plugin_2.12:2.0.2'
    }
  }

  apply plugin: 'com.github.alisiikh.scalastyle_2.12'

  scalaStyle {
    config = file("$rootDir/scalastyle_config.xml")
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
      classpath 'com.github.alisiikh:gradle-scalastyle-plugin_2.12:2.0.2'
    }
  }

  apply plugin: 'com.github.alisiikh.scalastyle_2.12'

  scalaStyle {
    config = file("$rootDir/scalastyle_config.xml")

    sourceSets {
      test {
        // specifically configure scalastyle for test sourceSet
        config = file("$rootDir/scalastyle_test.xml")
        failOnWarnings = true
      }

      intTest {
        // override output report for intTest sourceSet
        // but still use global scalastyle_config.xml
        output = file("$projectDir/scalastyle-intTest-check.xml")
      }
    }
  }
```

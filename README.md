# Scala Style Gradle Plugin

[![Build Status](https://travis-ci.org/ngbinh/gradle-scalastyle-plugin.svg?branch=master)](https://travis-ci.org/ngbinh/gradle-scalastyle-plugin)

### Instructions

```
maven repo: http://jcenter.bintray.com/
groupId: org.github.alisiikh.scalastyle
artifactId:  gradle-scalastyle-plugin_2.11
version: 1.0.1
```

Use `artifactId:  gradle-scalastyle-plugin_2.10` if you want to use with Scala `2.10`

```groovy
  apply plugin: 'scalaStyle'
```

Add following dependencies to your buildScript

```groovy
  classpath "org.github.alisiikh.scalastyle:gradle-scalastyle-plugin_2.12:2.0.0"
```

Configure the plugin

```groovy
scalaStyle {
  config = "/path/to/scalaStyle.xml"
}
```

Other optional properties are

```groovy
  outputFile  //Default => $buildDir/scala_style_result.xml
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
  apply plugin: 'scalaStyle'

  buildscript {
    repositories {
      jcenter() // only work after gradle 1.7
    }

    dependencies {
      classpath 'org.github.alisiikh.scalastyle:gradle-scalastyle-plugin_2.12:2.0.0'
    }
  }

  scalaStyle {
    config = "$rootDir/scalastyle_config.xml"

    sourceSets {
      test {
        config = "$rootDir/scalastyle_test.xml"
      }
    }
  }
```

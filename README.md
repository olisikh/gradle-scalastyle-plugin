# Scala Style Gradle Plugin

Gradle plugin for Scalastyle http://www.scalastyle.org/

### Instructions

https://plugins.gradle.org/plugin/com.github.alisiikh.scalastyle

Use:

```
plugins {
  id "com.github.alisiikh.scalastyle_2.10" version "2.0.0"
}
```
```
plugins {
  id "com.github.alisiikh.scalastyle_2.11" version "2.0.0"
}
```
```
plugins {
  id "com.github.alisiikh.scalastyle_2.12" version "2.0.0"
}
```

Or via buildScript:
```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.org.github.alisiikh.scalastyle:gradle-scalastyle-plugin_2.10:2.0.0" // or 2.11, 2.12
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
  apply plugin: 'scalaStyle'

  buildscript {
    repositories {
      jcenter()
    }

    dependencies {
      classpath 'org.github.alisiikh.scalastyle:gradle-scalastyle-plugin_2.12:2.0.0'
    }
  }

  scalaStyle {
    config = file("$rootDir/scalastyle_config.xml")
  }
```

#### Custom configuration per sourceSet
```
  apply plugin: 'scalaStyle'

  buildscript {
    repositories {
      jcenter()
    }

    dependencies {
      classpath 'org.github.alisiikh.scalastyle:gradle-scalastyle-plugin_2.12:2.0.0'
    }
  }

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

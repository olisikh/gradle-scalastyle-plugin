package org.github.ngbinh.scalastyle

import groovy.transform.ToString
import org.gradle.api.NamedDomainObjectContainer


abstract class BaseScalaStyle {
    File config
    File output
    String outputEncoding = "UTF-8"
    Boolean failOnViolation = true
    Boolean failOnWarning = false
    Boolean verbose = false
    Boolean quiet = false
    String inputEncoding = "UTF-8"
}

@ToString
class ScalaStyleSourceSet extends BaseScalaStyle {
    String name

    ScalaStyleSourceSet(String name) {
        this.name = name
    }
}

@ToString
class ScalaStyle extends BaseScalaStyle {
    Boolean skip = false

    NamedDomainObjectContainer<ScalaStyleSourceSet> sourceSets

    ScalaStyle(NamedDomainObjectContainer<ScalaStyleSourceSet> sourceSets) {
        this.sourceSets = sourceSets
    }

    def sourceSets(final Closure configureClosure) {
        sourceSets.configure(configureClosure)
    }
}

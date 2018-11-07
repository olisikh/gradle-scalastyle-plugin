package org.github.ngbinh.scalastyle

import org.gradle.api.NamedDomainObjectContainer

abstract class BaseScalaStyle {
    Boolean skip
    File config
    String outputEncoding
    Boolean failOnViolation
    Boolean failOnWarning
    Boolean verbose
    Boolean quiet
    String inputEncoding
}

class ScalaStyleSourceSet extends BaseScalaStyle {
    String name
    File output

    ScalaStyleSourceSet(String name) {
        this.name = name
    }
}

class ScalaStyle extends BaseScalaStyle {

    NamedDomainObjectContainer<ScalaStyleSourceSet> sourceSets

    ScalaStyle(NamedDomainObjectContainer<ScalaStyleSourceSet> sourceSets) {
        this.sourceSets = sourceSets
    }

    def sourceSets(final Closure configureClosure) {
        sourceSets.configure(configureClosure)
    }
}

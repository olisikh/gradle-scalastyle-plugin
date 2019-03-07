/*
 *    Copyright 2019. Oleksii Lisikh
 *
 *    Copyright 2014. Binh Nguyen
 *
 *    Copyright 2013. Muhammad Ashraf
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.github.alisiikh.scalastyle

import org.gradle.api.NamedDomainObjectContainer

abstract class BaseScalastyle {
    Boolean skip
    File config
    String inputEncoding
    String outputEncoding
    Boolean failOnWarning
    Boolean verbose
    Boolean quiet
}

class ScalastyleSourceSet extends BaseScalastyle {
    String name
    File output

    ScalastyleSourceSet(String name) {
        this.name = name
    }
}

class Scalastyle extends BaseScalastyle {
    String scalaVersion = '2.12'
    String scalastyleVersion = '1.0.0'

    NamedDomainObjectContainer<ScalastyleSourceSet> sourceSets

    Scalastyle(NamedDomainObjectContainer<ScalastyleSourceSet> sourceSets) {
        this.sourceSets = sourceSets
    }

    def sourceSets(final Closure c) {
        sourceSets.configure(c)
    }
}

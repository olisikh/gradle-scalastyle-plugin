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
package com.github.alisiikh.scalastyle

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.scala.ScalaPlugin
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

abstract class CommonScalastyleConfig {
    final Property<Boolean> skip
    final Property<File> config
    final Property<Boolean> failOnWarning

    CommonScalastyleConfig(Project project) {
        skip = project.objects.property(Boolean)
        config = project.objects.property(File)
        failOnWarning = project.objects.property(Boolean)
    }

    void setSkip(boolean skip) {
        this.skip.set(skip)
    }

    void setConfig(File config) {
        this.config.set(config)
    }

    void setFailOnWarning(boolean failOnWarning) {
        this.failOnWarning.set(failOnWarning)
    }
}

class SourceSetScalastyleConfig extends CommonScalastyleConfig {
    final String name

    final Property<File> output

    SourceSetScalastyleConfig(Project project, String name) {
        super(project)

        this.name = name

        output = project.objects.property(File)
        output.convention(new File(project.buildDir, "scalastyle/${name}/scalastyle-check.xml"))
    }

    void setOutput(File output) {
        this.output.set(output)
    }
}

class ScalastyleExtension extends CommonScalastyleConfig {
    static final SCALA_VERSION = '2.12'
    static final SCALASTYLE_VERSION = '1.2.0'

    final Property<String> scalaVersion
    final Property<String> scalastyleVersion
    final Property<String> inputEncoding
    final Property<String> outputEncoding
    final Property<Boolean> verbose
    final Property<Boolean> quiet
    final ListProperty<String> jvmArgs

    final NamedDomainObjectContainer<SourceSetScalastyleConfig> sourceSets

    ScalastyleExtension(Project project) {
        super(project)

        project.plugins.apply(JavaPlugin)
        project.plugins.apply(ScalaPlugin)

        scalaVersion = project.objects.property(String)
        scalaVersion.set(SCALA_VERSION)

        scalastyleVersion = project.objects.property(String)
        scalastyleVersion.set(SCALASTYLE_VERSION)

        inputEncoding = project.objects.property(String)
        inputEncoding.set('UTF-8')

        outputEncoding = project.objects.property(String)
        outputEncoding.set('UTF-8')

        verbose = project.objects.property(Boolean)
        verbose.set(false)

        quiet = project.objects.property(Boolean)
        quiet.set(false)

        jvmArgs = project.objects.listProperty(String)
        jvmArgs.set([])

        skip.convention(false)
        config.convention(new File(project.projectDir, "scalastyle_config.xml"))
        failOnWarning.convention(false)

        sourceSets = project.container(SourceSetScalastyleConfig, { name ->
            new SourceSetScalastyleConfig(project, name)
        })
    }

    void setScalaVersion(String scalaVersion) {
        this.scalaVersion.set(scalaVersion)
    }

    void setScalastyleVersion(String scalastyleVersion) {
        this.scalastyleVersion.set(scalastyleVersion)
    }

    void setInputEncoding(String encoding) {
        inputEncoding.set(encoding)
    }

    void setOutputEncoding(String encoding) {
        outputEncoding.set(encoding)
    }

    void setVerbose(boolean verbose) {
        this.verbose.set(verbose)
    }

    void setQuiet(boolean quiet) {
        this.quiet.set(quiet)
    }

    def sourceSets(final Closure c) {
        sourceSets.configure(c)
    }
}

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

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet

class ScalastylePlugin implements Plugin<Project> {

    static final SCALASTYLE_CHECK = 'scalastyleCheck'

    private Project project
    private ScalastyleExtension extension

    void apply(Project project) {
        this.project = project
        this.extension = project.extensions.create('scalastyle', ScalastyleExtension, project)

        project.configurations.create('scalastyle')
                .setVisible(false)
                .setTransitive(true)
                .setDescription('Scalastyle libraries to be used for this project.')

        project.afterEvaluate { p ->
            p.dependencies {
                // scala is already included in scalastyle dependency transitively
                scalastyle "org.scalastyle:scalastyle_${extension.scalaVersion.get()}:${extension.scalastyleVersion.get()}"
            }
        }

        createTasks()
    }

    private def createTasks() {
        def scalastyleCheckTask = project.tasks.register(SCALASTYLE_CHECK)

        project.afterEvaluate {
            def scalastyleTasks = project.sourceSets.findResults { SourceSet sourceSet ->
                if (sourceSet.scala.srcDirs.isEmpty()) {
                    return null
                }

                def sourceSetConfig = getOrCreateSourceSetConfig(sourceSet)
                def shouldSkipTask = sourceSetConfig.skip.isPresent() ? sourceSetConfig.skip : extension.skip

                project.tasks.create("scalastyle${sourceSet.name.capitalize()}Check", ScalastyleCheckTask) {
                    group = 'verification'
                    description = "Runs scalastyle checks on ${sourceSet.name} source set."

                    onlyIf { !shouldSkipTask.get() }

                    source = sourceSet.scala.srcDirs
                    output = sourceSetConfig.output
                    config = resolveScalastyleConfig(sourceSetConfig, sourceSet.name)
                    failOnWarning = sourceSetConfig.failOnWarning.isPresent() ?
                            sourceSetConfig.failOnWarning : extension.failOnWarning
                    verbose = extension.verbose
                    quiet = extension.quiet
                    outputEncoding = extension.outputEncoding
                    inputEncoding = extension.inputEncoding
                }
            }

            scalastyleCheckTask.configure {
                group = 'verification'
                description = 'Runs scalastyle checks.'
                dependsOn(scalastyleTasks)
            }
            project.check.dependsOn(scalastyleCheckTask)
        }
    }

    private def getOrCreateSourceSetConfig(SourceSet sourceSet) {
        extension.sourceSets.findByName(sourceSet.name) ?: extension.sourceSets.create(sourceSet.name)
    }

    private def resolveScalastyleConfig(SourceSetScalastyleConfig sourceSetConfig, String sourceSetName) {
        def config = sourceSetConfig.config.isPresent() ? sourceSetConfig.config : extension.config

        def configFile = config.get()
        if (!configFile.exists() || configFile.isDirectory()) {
            throw new GradleException("Scalastyle configuration file does not exist at path $configFile")
        } else {
            project.logger.info("Using scalastyle configuration ${configFile} for ${sourceSetName} source set")
        }

        config
    }
}

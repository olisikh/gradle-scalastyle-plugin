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

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.scala.ScalaPlugin
import org.gradle.api.tasks.SourceTask
import org.gradle.process.internal.ExecException


class ScalastylePlugin implements Plugin<Project> {
    private Project project
    private Scalastyle extension

    void apply(Project project) {
        this.project = project
        this.extension = new Scalastyle(project.container(ScalastyleSourceSet))

        project.plugins.apply(ScalaPlugin)

        project.extensions.add('scalastyle', extension)
        project.configurations.create('scalastyle')
                .setVisible(false)
                .setTransitive(true)
                .setDescription('Scalastyle libraries to be used for this project.')

        project.afterEvaluate { p ->
            p.dependencies {
                // scala is already included in scalastyle dependency transitively
                scalastyle "org.scalastyle:scalastyle_${extension.scalaVersion}:${extension.scalastyleVersion}"
            }
        }

        setupScalaStyle()
    }

    private def setupScalaStyle() {
        def scalastyleCheckTask = project.task('scalastyleCheck') {
            group = 'verification'
            description = 'Runs scalastyle checks.'

            project.afterEvaluate {
                setupExtensionDefaults()

                def sourceSets = project.sourceSets.findAll { !it.scala.srcDirs.isEmpty() }
                        .collectEntries { [it.name, it.scala.srcDirs] }

                def scalastyleTasks = sourceSets.findResults {
                    def sourceSetName = it.key as String
                    def srcDirs = it.value as List<File>

                    createScalaStyleTask(sourceSetName, srcDirs)
                }

                dependsOn(scalastyleTasks)
            }
        }

        project.check.dependsOn(scalastyleCheckTask)
    }

    private def createScalaStyleTask(String sourceSetName, List<File> srcDirs) {
        def overrides = extension.sourceSets.find { it.name == sourceSetName } ?:
                new ScalastyleSourceSet(sourceSetName)

        def skip = overrides.skip != null ? overrides.skip : extension.skip
        if (!skip) {
            project.task(type: SourceTask, "scalastyle${sourceSetName.capitalize()}Check") {
                group = 'verification'
                description = "Runs scalastyle checks on source set ${sourceSetName}."

                def outputFile = overrides.output ?
                        project.file(overrides.output) :
                        project.file("${project.buildDir.absolutePath}/scalastyle/${sourceSetName}/scalastyle-check.xml")

                source = srcDirs
                outputs.files(outputFile)

                def config = overrides.config ? overrides.config : extension.config
                if (!config.exists() || config.isDirectory()) {
                    throw new GradleException("Scalastyle configuration $config file does not exist")
                }

                logger.info("Using scalastyle configuration for ${sourceSetName} source set: ${config.absolutePath}")

                def options = extractOptions(overrides)

                doLast {
                    try {
                        project.javaexec {
                            main = 'org.scalastyle.Main'
                            args([
                                    '-c', config.absolutePath,
                                    '-v', options.verbose,
                                    '-q', options.quiet,
                                    '--xmlOutput', outputFile.absolutePath,
                                    '--xmlEncoding', options.outputEncoding,
                                    '--inputEncoding', options.inputEncoding,
                                    '-w', options.failOnWarning
                            ] + srcDirs.collect { it.absolutePath })

                            classpath = project.configurations.scalastyle
                        }
                    } catch (ExecException e) {
                        throw new GradleException("Scalastyle check failed for sourceSet $sourceSetName.")
                    }
                }
            }
        } else {
            project.logger.lifecycle("Skipping source set $sourceSetName")
            null
        }
    }

    private def setupExtensionDefaults() {
        extension.with {
            skip = skip == null ? false : skip
            inputEncoding = inputEncoding == null ? "UTF-8" : inputEncoding
            outputEncoding = outputEncoding == null ? "UTF-8" : outputEncoding
            verbose = verbose == null ? false : verbose
            quiet = quiet == null ? false : quiet
            failOnWarning = failOnWarning == null ? false : failOnWarning
        }
    }

    private def extractOptions(ScalastyleSourceSet overrides) {
        [
                inputEncoding : overrides.inputEncoding ?: extension.inputEncoding,
                outputEncoding: overrides.outputEncoding ?: extension.outputEncoding,
                verbose       : overrides.verbose != null ? overrides.verbose : extension.verbose,
                quiet         : overrides.quiet != null ? overrides.quiet : extension.quiet,
                failOnWarning : overrides.failOnWarning != null ? overrides.failOnWarning : extension.failOnWarning
        ]
    }
}


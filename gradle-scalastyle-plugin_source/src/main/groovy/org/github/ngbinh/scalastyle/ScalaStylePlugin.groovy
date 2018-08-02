/*
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
package org.github.ngbinh.scalastyle

import groovy.util.logging.Slf4j
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask
import org.scalastyle.ScalastyleConfiguration
import org.scalastyle.TextOutput
import org.scalastyle.XmlOutput

/**
 * @author Binh Nguyen
 * @since 12/16/2014
 * @author Muhammad Ashraf
 * @since 5/11/13
 */
@Slf4j
class ScalaStylePlugin implements Plugin<Project> {

    private ScalaStyleUtils scalaStyleUtils = new ScalaStyleUtils()

    void apply(Project project) {
        def extension = new ScalaStyle(project.container(ScalaStyleSourceSet))
        project.extensions.add('scalaStyle', extension)
        project.configurations.create('scalaStyle')
                .setVisible(false)
                .setTransitive(true)
                .setDescription('Scala Style libraries to be used for this project.')

        project.afterEvaluate {
            def globalConfig = extension.config ?
                    ScalastyleConfiguration.readFromXml(extension.config.absolutePath) :
                    null

            def srcSets = project.sourceSets.findAll { !it.scala.srcDirs.isEmpty() }
                    .collectEntries { [it.name, it.scala.srcDirs] }

            def scalaStyleTasks = srcSets.findResults {
                def sourceSetName = it.key
                def srcDirs = it.value

                def overrides = extension.sourceSets.find { it.name == sourceSetName } ?:
                        new ScalaStyleSourceSet(sourceSetName) // create dummy object to not fail with NPE

                def skip = overrides.skip != null ? overrides.skip : extension.skip
                if (!skip) {
                    project.task(type: SourceTask, "scalaStyle${sourceSetName.capitalize()}") {
                        group = 'verification'
                        description = "Runs scalastyle checks on source set ${sourceSetName}."

                        source = srcDirs

                        doLast {
                            try {
                                def startMs = System.currentTimeMillis()

                                def usedConfig = overrides.config ?
                                        ScalastyleConfiguration.readFromXml(overrides.config.absolutePath) :
                                        globalConfig

                                def encoding = overrides.inputEncoding ?: extension.inputEncoding
                                def filesToProcess = scalaStyleUtils.getFilesToProcess(
                                        srcDirs.collect { project.file(it) }, encoding)
                                def messages = scalaStyleUtils.checkFiles(usedConfig, filesToProcess)

                                def config = scalaStyleUtils.readConfig()
                                def verbose = overrides.verbose != null ? overrides.verbose : extension.verbose
                                def quiet = overrides.quiet != null ? overrides.quiet : extension.quiet

                                def outputResult = new TextOutput(config, verbose, quiet).output(messages)

                                def outputFile = project.file("${project.buildDir.absolutePath}/scalastyle/${sourceSetName}/scalastyle-check.xml")
                                log.debug("Saving to outputFile={}", outputFile.canonicalPath)
                                def outputEncoding = overrides.outputEncoding ?: extension.outputEncoding

                                XmlOutput.save(config, outputFile.absolutePath, outputEncoding, messages)

                                def stopMs = System.currentTimeMillis()
                                if (!quiet) {
                                    log.info("Processed {} file(s)", outputResult.files())
                                    log.warn("Found {} warnings", outputResult.warnings())
                                    log.error("Found {} errors", outputResult.errors())
                                    log.info("Finished in {} ms", stopMs - startMs)
                                }

                                def failOnWarning = overrides.failOnWarning != null ? overrides.failOnWarning : extension.failOnWarning

                                def violations = outputResult.errors() + ((failOnWarning) ? outputResult.warnings() : 0)
                                def failOnViolation = overrides.failOnViolation != null ? overrides.failOnViolation : extension.failOnViolation
                                if (violations > 0) {
                                    if (failOnViolation) {
                                        throw new Exception("You have " + violations + " Scalastyle violation(s).")
                                    } else {
                                        log.warn("Scalastyle:check violations detected but failOnViolation set to " + failOnViolation)
                                    }
                                } else {
                                    log.debug("Scalastyle:check no violations found")
                                }
                            } catch (Exception ex) {
                                throw new Exception("Scalastyle check error", ex)
                            }
                        }
                    }
                } else {
                    null
                }
            }

            project.task('scalaStyle') {
                group = 'verification'
                description = "Runs scalastyle checks."

                dependsOn scalaStyleTasks
            }
        }
    }
}

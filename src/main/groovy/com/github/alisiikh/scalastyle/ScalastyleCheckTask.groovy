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
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.*
import org.gradle.process.JavaForkOptions
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor

import javax.inject.Inject

@CacheableTask
class ScalastyleCheckTask extends SourceTask {
    private final WorkerExecutor workerExecutor;

    @Inject
    ScalastyleCheckTask(WorkerExecutor workerExecutor) {
        this.workerExecutor = workerExecutor;
    }

    @PathSensitive(PathSensitivity.RELATIVE)
    @InputFile
    final Property<File> config = project.objects.property(File)

    @PathSensitive(PathSensitivity.RELATIVE)
    @InputFiles
    final SetProperty<File> sourceDirs = project.objects.setProperty(File)

    @OutputFile
    final Property<File> output = project.objects.property(File)

    @Input
    final Property<String> inputEncoding = project.objects.property(String)
    @Input
    final Property<String> outputEncoding = project.objects.property(String)
    @Input
    final Property<Boolean> failOnWarning = project.objects.property(Boolean)
    @Input
    final Property<Boolean> verbose = project.objects.property(Boolean)
    @Input
    final Property<Boolean> quiet = project.objects.property(Boolean)

    @Internal
    final Property<JavaForkOptions> options = project.objects.property(JavaForkOptions)

    @TaskAction
    def run() {
        try {
            def arguments = [
              '-c', config.get().absolutePath,
              '-v', verbose.get().toString(),
              '-q', quiet.get().toString(),
              '--xmlOutput', output.get().absolutePath,
              '--xmlEncoding', outputEncoding.get(),
              '--inputEncoding', inputEncoding.get(),
              '-w', failOnWarning.get().toString()
            ]

            def srcDirs = sourceDirs.get().collect { it.absolutePath }.toList()

            logger.debug("Arguments to be used by Scalastyle: ${arguments.join(" ")}")
            logger.debug("""Source folders to be inspected by Scalastyle:
                           |${srcDirs.join(System.lineSeparator())}""".stripMargin())

            WorkQueue workQueue = workerExecutor.processIsolation() { workerSpec ->
                workerSpec.getClasspath().from(getProject().getConfigurations().getByName('scalastyle'))
                options.get().copyTo(workerSpec.getForkOptions())
            }
            workQueue.submit(ScalastyleCheckAction.class) { parameters ->
                parameters.getArgs().set([
                  '-c', config.get().absolutePath,
                  '-v', verbose.get().toString(),
                  '-q', quiet.get().toString(),
                  '--xmlOutput', output.get().absolutePath,
                  '--xmlEncoding', outputEncoding.get(),
                  '--inputEncoding', inputEncoding.get(),
                  '-w', failOnWarning.get().toString()
                ] + srcDirs)
            }
        } catch(Throwable e) {
            throw new GradleException("Failed to execute Scalastyle inspection", e)
        }
    }
}

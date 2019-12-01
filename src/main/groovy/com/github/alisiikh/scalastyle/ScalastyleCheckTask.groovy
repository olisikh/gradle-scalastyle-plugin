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
import org.gradle.api.tasks.*
import org.gradle.process.internal.ExecException

@CacheableTask
class ScalastyleCheckTask extends SourceTask {

    @PathSensitive(PathSensitivity.ABSOLUTE)
    @InputFile
    final Property<File> config = project.objects.property(File)

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

    @TaskAction
    def run() {
        try {
            project.javaexec {
                main = 'org.scalastyle.Main'
                args([
                        '-c', config.get(),
                        '-v', verbose.get(),
                        '-q', quiet.get(),
                        '--xmlOutput', output.get(),
                        '--xmlEncoding', outputEncoding.get(),
                        '--inputEncoding', inputEncoding.get(),
                        '-w', failOnWarning.get()
                ] + source.collect { it.absolutePath })

                classpath = project.configurations.scalastyle
            }
        } catch (ExecException e) {
            throw new GradleException("Scalastyle check failed.")
        }
    }
}

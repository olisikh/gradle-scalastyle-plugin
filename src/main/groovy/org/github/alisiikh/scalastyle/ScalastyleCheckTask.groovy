package org.github.alisiikh.scalastyle

import org.gradle.api.GradleException
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.process.internal.ExecException

@CacheableTask
class ScalastyleCheckTask extends SourceTask {

    @InputFile
    Property<File> config = project.objects.property(File)
    @OutputFile
    Property<File> output = project.objects.property(File)
    @Input
    Property<String> inputEncoding = project.objects.property(String)
    @Input
    Property<String> outputEncoding = project.objects.property(String)
    @Input
    Property<Boolean> failOnWarning = project.objects.property(Boolean)
    @Input
    Property<Boolean> verbose = project.objects.property(Boolean)
    @Input
    Property<Boolean> quiet = project.objects.property(Boolean)

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

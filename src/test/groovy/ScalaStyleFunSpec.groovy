import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Shared

abstract class ScalaStyleFunSpec extends ScalaStyleSpec {

    @Shared
    List<File> pluginClasspath

    def setupSpec() {
        def current = getClass().getResource("/").file
        pluginClasspath = [
                current.replace("classes/test", "classes/main"),
                current.replace("classes/test", "resources/main")
        ].collect { new File(it) }
    }

    File prepareTest(String scalaVersion) {
        createBuildFolder(scalaVersion)
    }

    BuildResult executeGradle(String task) {
        GradleRunner.create().forwardOutput()
                .withProjectDir(testProjectDir.getRoot())
                .withPluginClasspath(pluginClasspath)
                .withArguments("--stacktrace", task)
                .withDebug(true)
                .build()
    }

    BuildResult executeGradle(String task, String gradleVersion) {
        GradleRunner.create().forwardOutput()
                .withProjectDir(testProjectDir.getRoot())
                .withPluginClasspath(pluginClasspath)
                .withArguments("--stacktrace", task)
                .withDebug(true)
                .withGradleVersion(gradleVersion)
                .forwardOutput()
                .build()
    }
}

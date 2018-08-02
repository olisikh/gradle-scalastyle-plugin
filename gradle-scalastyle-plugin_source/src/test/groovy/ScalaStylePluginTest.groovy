import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class ScalaStylePluginTest extends Specification {
    def "add tasks to the project"() {
        when:
        Project project = ProjectBuilder.builder().build()
        project.plugins.apply "scalaStyle"

        then:
        project.tasks.scalaStyle != null
    }
}

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

abstract class ScalaStyleSpec extends Specification {
    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder()

    @Shared
    File testProjectBuildDir

    def setup() {
        println(testProjectDir.root.absolutePath)
    }

    def createBuildFolder(String scalaVersion) {
        def projectFolder = new File(this.class.getResource(
                "/zinc-${scalaVersion.replace('.', '_')}").file)

        FileUtils.copyDirectory(projectFolder, testProjectDir.root)
        testProjectBuildDir = new File(testProjectDir.root, "build")
    }
}

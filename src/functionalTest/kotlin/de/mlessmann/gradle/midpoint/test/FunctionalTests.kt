package de.mlessmann.gradle.midpoint.test

import de.mlessmann.gradle.midpoint.test.matchers.aFile
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class FunctionalTests {

    @TempDir
    lateinit var testProjectDir: File

    @Test
    fun testTransformXML() {
        val gradleProjectDir = setupFixture("passes_transformXML")
        val result = execGradle(gradleProjectDir, "transformXML")

        val outputDir = File(gradleProjectDir, "build/output")
        val expectedDir = File(gradleProjectDir, "expected")
        val templates = listOf(*expectedDir.listFiles()!!)
        templates.forEach { templateFile ->
            val challenger = File(outputDir, templateFile.name)
            assertThat(challenger, `is`(aFile()))

            val template = templateFile.inputStream().use { it.reader().buffered().readText() }
            val response = challenger.inputStream().use { it.reader().buffered().readText() }
            assertThat(response, equalTo(template))
        }
    }

    /**
     * Copies test fixture into temp directory under test.
     */
    private fun setupFixture(fixtureName: String, projectName: String = fixtureName): File {
        val fixtureDir = File("src/functionalTest/fixtures", fixtureName)
        val projectDir = File(testProjectDir, projectName)
        assertThat(projectDir.mkdirs(), equalTo(true))

        fixtureDir.copyRecursively(projectDir, overwrite = true)

        return projectDir
    }

    private fun execGradle(dir: File, vararg commands: String): BuildResult? {
        return GradleRunner.create()    //
            .withPluginClasspath()      //
            .withProjectDir(dir)        //
            .withDebug(true)            //
            .forwardOutput()            //
            .withArguments(*commands, "--stacktrace", "--debug").build()
    }
}
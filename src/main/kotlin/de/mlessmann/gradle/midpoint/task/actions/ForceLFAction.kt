package de.mlessmann.gradle.midpoint.task.actions

import de.mlessmann.gradle.midpoint.MidpointGradlePlugin.Companion.LOG_PRE
import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.slf4j.Logger
import java.io.File

class ForceLFAction(private val forceLFProperty: Property<Boolean>, private val logger: Logger) : Action<Task> {

    override fun execute(task: Task) {
        if (!forceLFProperty.get()) {
            logger.warn("[$LOG_PRE] LF correction is disabled. Please remember that Gradle's CopyTask currently forces system line endings when filters are used.")
            return
        }
        if (System.lineSeparator() == "\n") {
            logger.info("[$LOG_PRE] Not forcing LF line endings, we appear to be using that already.")
            return
        }

        logger.info("[$LOG_PRE] Forcing LF for task output of ${task.name}")
        task.outputs.files.forEach(this::fixOutputLineBreaks)
    }

    private fun fixOutputLineBreaks(fileOrDir: File) {
        if (fileOrDir.isFile) {
            fixFileLineBreaks(fileOrDir)
        } else {
            fileOrDir.walkTopDown().forEach {
                if (it.isFile) {
                    fixFileLineBreaks(it)
                }
            }
        }
    }

    private fun fixFileLineBreaks(outputFile: File) {
        logger.debug("\tFORCE LF: ${outputFile.path}")
        val content = outputFile.readText();
        outputFile.writeText(content.replace("\r\n", "\n"))
    }

}
package de.mlessmann.gradle.midpoint.task.xml

import org.gradle.api.internal.file.FileResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

private val WIN_ROOT_PATH_PATTERN = Regex("^[a-z]:", RegexOption.IGNORE_CASE)

class InclusionResolver(private val fileResolver: FileResolver) {

    private val logger: Logger = LoggerFactory.getLogger(InclusionResolver::class.java)

    fun resolve(path: String, source: TransformationDetails): String {
        if (path.startsWith("/") || WIN_ROOT_PATH_PATTERN.matches(path)) {
            throw IllegalArgumentException("Absolute paths are not supported! Use './' or '../' for paths relative to the file.")
        } else if (path.startsWith("./" ) || path.startsWith("../")) {
            val relativeResolver = fileResolver.newResolver(source.file.parent.toFile().absoluteFile)
            val file = relativeResolver.resolve(path)
            logger.debug("Resolved '{}' to: {}", path, file.toPath())

            val childDetails = TransformationDetails(file.toPath(), null, false)
            val childAction = TransformFileAction(this)
            childAction.execute(childDetails)
            return childDetails.output!!

        } else {
            throw IllegalArgumentException("Unsupported inclusion path: $path")
        }
    }
}
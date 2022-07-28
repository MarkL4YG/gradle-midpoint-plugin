package de.mlessmann.gradle.midpoint.task.xml

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.*
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
@CacheableTask
abstract class TransformXMLTask @Inject constructor(
    objectFactory: ObjectFactory,
    private val fileResolver: FileResolver
) : DefaultTask() {

    private val logger = LoggerFactory.getLogger(TransformXMLTask::class.java)

    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    @get:InputDirectory
    val inputDirectory: DirectoryProperty = objectFactory.directoryProperty()

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty = objectFactory.directoryProperty()

    @TaskAction
    fun gradleExecuteTransform() {
        logger.debug("Hello from XML transformer!")
        inputDirectory.finalizeValue()

        val inclusionResolver = InclusionResolver(fileResolver)
        val action = TransformFileAction(inclusionResolver)

        inputDirectory.asFileTree
            .filter { it.isFile }
            .map { prepareFile(it.absoluteFile) }
            .forEach { action.execute(it) }

        outputDirectory.finalizeValue()
    }

    protected fun prepareFile(file: File): TransformationDetails {
        val filePath = file.absoluteFile.toPath()
        val generatedOutputPath = generateOutputPath(file)
        return TransformationDetails(filePath, generatedOutputPath, true)
    }

    protected fun generateOutputPath(file: File): Path {
        val absoluteBase = inputDirectory.get().asFile.absoluteFile
        val relativeFile = file.relativeTo(absoluteBase)
        val outputFile = outputDirectory.get().file(relativeFile.path).asFile
        return outputFile.absoluteFile.toPath()
    }

    fun from(filePath: String) {
        from(fileResolver.resolve(filePath))
    }

    fun from(file: File) {
        inputDirectory.set(file)
    }

    fun into(file: File) {
        outputDirectory.set(file)
    }

    fun into(path: String) {
        return into(fileResolver.resolve(path))
    }
}
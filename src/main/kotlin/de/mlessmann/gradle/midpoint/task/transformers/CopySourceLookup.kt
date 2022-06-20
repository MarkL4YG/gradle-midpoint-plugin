package de.mlessmann.gradle.midpoint.task.transformers

import org.gradle.api.file.FileCopyDetails
import java.io.File

class CopySourceLookup() {
    private val sourceMapping = HashMap<String, String>()

    fun remember(copyDetails: FileCopyDetails) {
        sourceMapping[copyDetails.sourcePath] = copyDetails.file.absolutePath
    }

    fun findSourceFile(copyDetails: FileCopyDetails): String? {
        return sourceMapping[copyDetails.sourcePath]
    }

    fun getSourceFile(copyDetails: FileCopyDetails): File {
        val path = sourceMapping[copyDetails.sourcePath] ?: error("No source file available for sourcePath \"$copyDetails\". Has this file been injected into the copy action?")
        return File(path)
    }
}
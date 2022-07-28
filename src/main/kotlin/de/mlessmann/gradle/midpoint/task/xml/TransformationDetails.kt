package de.mlessmann.gradle.midpoint.task.xml

import java.nio.file.Path
import javax.inject.Inject

class TransformationDetails(val file: Path, val outputFile: Path?, private val isSourceFile: Boolean) {

    var output: String? = null

    fun isCacheOutput(): Boolean {
        return !isSourceFile
    }
}
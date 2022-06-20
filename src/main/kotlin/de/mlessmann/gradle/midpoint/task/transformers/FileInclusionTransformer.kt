package de.mlessmann.gradle.midpoint.task.transformers

import de.mlessmann.gradle.midpoint.MidpointGradlePlugin.Companion.LOG_PRE
import org.gradle.api.Transformer
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.internal.file.FileResolver
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

@JvmField
val INCLUSION_PATTERN = Pattern.compile("\\$\\(@(.+?)\\)");

open class FileInclusionTransformer(private val fileResolver: FileResolver, private val sourceLookup: CopySourceLookup, private val fileCopyDetails: FileCopyDetails) :
    Transformer<String, String> {

    private val logger = LoggerFactory.getLogger(FileInclusionTransformer::class.java);

    override fun transform(line: String): String {
        if (line.length < 5) {
            return line;
        }

        val matcher = INCLUSION_PATTERN.matcher(line);
        if (!matcher.find()) {
            return line;
        }

        val filePath = matcher.group(1);
        logger.debug("Found inclusion pattern in line '{}' <== '{}'", filePath, line);
        val sourceFile = sourceLookup.getSourceFile(fileCopyDetails)
        val relativeResolver = fileResolver.newResolver(sourceFile.parentFile);
        val includedFile = relativeResolver.resolve(filePath);

        if (!includedFile.isFile) {
            error("Included file could not be found \"$filePath\". Included by ${sourceFile.path}");
        }

        val placeable = includedFile.inputStream().use { it.reader().buffered().readText() }
        logger.info("[$LOG_PRE] Including $filePath into ${sourceFile.path}")
        return placeable
    }
}
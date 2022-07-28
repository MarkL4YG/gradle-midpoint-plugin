package de.mlessmann.gradle.midpoint.task.xml

import org.gradle.api.Action
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter
import kotlin.io.path.isDirectory

val DEP_PATTERN_INCLUDE = Regex("\\\$\\(@(.+)\\)")
val WARN_LEGACY_INCL = AtomicBoolean(false)
val DEP_PATTERN_TIMESTAMP = Regex("\\\$currentTimeStamp")
val WARN_LEGACY_TSP = AtomicBoolean(false)

class TransformFileAction(private val inclusionResolver: InclusionResolver) : Action<TransformationDetails> {

    private val logger: Logger = LoggerFactory.getLogger(TransformXMLTask::class.java)

    override fun execute(t: TransformationDetails) {
        logger.debug("Reading file: ${t.file}")
        val content = t.file.bufferedReader().use {
            it.readText()
        }
        t.output = content

        legacyReplacement(t)
        legacyTimestamp(t)

        if (t.outputFile != null && t.output != null) {
            logger.debug("Writing file: ${t.outputFile}")
            if (t.outputFile.parent.isDirectory()) {
                Files.createDirectories(t.outputFile.parent)
            }

            t.outputFile.bufferedWriter().use {
                it.write(t.output!!)
            }
        }

        if (!t.isCacheOutput()) {
            t.output = null
        }
    }

    private fun legacyReplacement(t: TransformationDetails) {
        val processedText = t.output!!.lines().map { line ->
            val match = DEP_PATTERN_INCLUDE.find(line) ?: return@map line

            if (!WARN_LEGACY_INCL.getAndSet(true)) {
                logger.warn("LEGACY INCLUSION USED: The @($...) inclusion syntax is incompatible with the XML format spec and is no longer recommended. Use PUT NEW PATTERN HERE!!! instead.")
            }

            val includePath = match.groupValues[1]
            return@map inclusionResolver.resolve(includePath, t)
        }.joinToString("\n")
        t.output = processedText
    }

    private fun legacyTimestamp(t: TransformationDetails) {
        val processedText = t.output!!.lines().map { line ->
            val match = DEP_PATTERN_TIMESTAMP.find(line) ?: return@map line

            if (!WARN_LEGACY_TSP.getAndSet(true)) {
                logger.warn("LEGACY TIMESTAMP USED: The \$currentTime syntax is incompatible with the XML format spec and is no longer recommended. Use PUT NEW PATTERN HERE!!! instead.")
            }

            val tsp = ZonedDateTime.now().withFixedOffsetZone().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            return@map line.replaceRange(match.range, tsp)
        }.joinToString("\n")
        t.output = processedText
    }
}
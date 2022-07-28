package de.mlessmann.gradle.midpoint

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.internal.VersionNumber
import org.slf4j.LoggerFactory

class MidpointGradlePlugin: Plugin<Project> {

    private val logger = LoggerFactory.getLogger(MidpointGradlePlugin::class.java)

    override fun apply(project: Project) {
        val runningGradleVersion = VersionNumber.parse(project.gradle.gradleVersion)
        if (runningGradleVersion < MIN_GRADLE_VERSION) {
            logger.warn("[$LOG_PRE] This plugin has been testet with Gradle 7.3.3 and might not run on your version (${runningGradleVersion})!")
        }
        logger.info("[$LOG_PRE] Hello World!")
    }

    companion object {
        const val LOG_PRE = "midpoint-gradle-plugin"
        val MIN_GRADLE_VERSION: VersionNumber = VersionNumber.parse("7.3.3")
    }
}

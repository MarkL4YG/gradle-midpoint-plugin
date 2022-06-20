plugins {
    id("de.mlessmann.gradle.midpoint-plugin")
}

import de.mlessmann.gradle.midpoint.task.TransformXMLTask

tasks.register<TransformXMLTask>("transformXML") {
    from("input")
    into(File(project.buildDir, "output"))

    outputs.cacheIf { false }
}

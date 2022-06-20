package de.mlessmann.gradle.midpoint.task

import de.mlessmann.gradle.midpoint.task.actions.ForceLFAction
import de.mlessmann.gradle.midpoint.task.transformers.CopySourceLookup
import de.mlessmann.gradle.midpoint.task.transformers.FileInclusionTransformer
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Input

open class TransformXMLTask : Copy() {

    private val copySourceLookup = CopySourceLookup()

    @Input
    val forceLFBreaks: Property<Boolean> = objectFactory.property(Boolean::class.java)

    init {
        forceLFBreaks.set(true)
        this.eachFile() {
            // Early visitor: Remember all the original source paths.
            // Required because FileCopyDetails rejects access to the original file reference once any filter has been added...
            copySourceLookup.remember(it)
        }
        this.eachFile() {
            it.filter(FileInclusionTransformer(fileResolver, copySourceLookup, it))
        }
        this.doLast(ForceLFAction(forceLFBreaks, logger))
    }
}
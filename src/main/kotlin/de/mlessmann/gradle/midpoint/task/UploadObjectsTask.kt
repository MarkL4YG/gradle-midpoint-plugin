package de.mlessmann.gradle.midpoint.task

import de.mlessmann.gradle.midpoint.DefaultValues
import de.mlessmann.gradle.midpoint.MidpointAPI
import de.mlessmann.gradle.midpoint.except.HttpRequestException
import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

open class UploadObjectsTask(objectFactory: ObjectFactory) : DefaultTask() {

    val midPointUrl: Property<URI> = objectFactory.property(URI::class.java)

    val midPointUser: Property<String> = objectFactory.property(String::class.java)

    val midPointPass: Property<String> = objectFactory.property(String::class.java)

    val ignoreFileExtension: Property<Boolean> = objectFactory.property(Boolean::class.java)

    init {
        initDefaultProperties()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun initDefaultProperties() {
        midPointUrl.set(URI.create(DefaultValues.MP_HOST))
        midPointUser.set(DefaultValues.MP_USER)
        midPointPass.set(DefaultValues.MP_PASS)
    }

    @TaskAction
    fun uploadFiles() {
        val httpClient = HttpClient.newBuilder().build()

        inputs.files.filter { it.isFile && it.exists() }
            .filter { ignoreFileExtension.get() || it.name.endsWith(".xml") }.forEach {
                val request = HttpRequest.newBuilder(MidpointAPI.updateObject(midPointUrl.get()))
                    .POST(HttpRequest.BodyPublishers.ofFile(it.toPath())).build()

                httpClient.send(request) { response ->
                    if (response.statusCode() in 200..299) {
                        logger.debug("Successfully uploaded file: ${it.path}")
                    } else {
                        throw HttpRequestException("Upload of file failed (status=${response.statusCode()}): ${it.path}")
                    }
                    HttpResponse.BodySubscribers.discarding()
                }
            }
    }
}
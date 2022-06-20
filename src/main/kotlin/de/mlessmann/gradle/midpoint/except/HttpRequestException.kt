package de.mlessmann.gradle.midpoint.except

class HttpRequestException(message: String): RuntimeException(message) {

    constructor(message: String, cause: Throwable): this(message) {
        initCause(cause)
    }
}
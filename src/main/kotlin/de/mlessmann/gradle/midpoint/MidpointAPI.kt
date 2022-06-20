package de.mlessmann.gradle.midpoint

import java.net.URI

object MidpointAPI {

    fun createObject(mpHost: URI): URI {
        return mpHost.resolve("/ws/rest")
    }

    fun updateObject(mpHost: URI, oid: String = ""): URI {
        return mpHost.resolve("/ws/rest")
    }
}
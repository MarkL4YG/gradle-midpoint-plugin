package de.mlessmann.gradle.midpoint.base.http

import java.net.Authenticator

class BasicAuthenticator(val username: String, val password: String): Authenticator() {


}
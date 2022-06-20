package de.mlessmann.gradle.midpoint.test.matchers

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import java.io.File

fun aFile(): IsAFileMatcher {
    return IsAFileMatcher()
}

class IsAFileMatcher: TypeSafeMatcher<File>() {

    override fun describeTo(description: Description?) {
        description!!.appendText("An existing file")
    }

    override fun matchesSafely(item: File?): Boolean {
        return item != null && item.exists() && item.isFile
    }
}
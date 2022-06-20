plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "1.6.10"
}

group = "de.mlessmann.gradle"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins {
        create("midpoint-plugin") {
            id = "de.mlessmann.gradle.midpoint-plugin"
            implementationClass = "de.mlessmann.gradle.midpoint.MidpointGradlePlugin"
        }
    }
}

val functionalTestSrc = sourceSets.create("functionalTest") {
    compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
    runtimeClasspath += output + compileClasspath /* configurations["testRuntimeClasspath"]*/
}
val functionalTestImpl = configurations.getByName("functionalTestImplementation") //
    .extendsFrom(configurations.getByName("testImplementation"))

dependencies {
    compileOnly(kotlin("gradle-plugin", "1.6.10"))

    implementation(kotlin("stdlib", "1.6.10"))
    implementation(gradleApi())

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.8.2")
}

val functionalTest by tasks.registering(Test::class) {
    description = "Runs functional tests on Gradle plugin level."
    group = "verification"

    testClassesDirs = functionalTestSrc.output.classesDirs
    classpath = functionalTestSrc.runtimeClasspath

    testLogging.showStandardStreams = true

    beforeTest(
        closureOf<TestDescriptor> {
            logger.lifecycle("Starting test: $this")
        }
    )
}

tasks.withType<Test>().configureEach {
    // Using JUnit 5 Platform for running tests
    useJUnitPlatform()
}

tasks.check {
    dependsOn(functionalTest)
}

tasks.getByName<Wrapper>("wrapper") {
    gradleVersion = "7.3.3"
    distributionType = Wrapper.DistributionType.ALL
    distributionUrl = "https://services.gradle.org/distributions/gradle-7.3.3-all.zip"
}
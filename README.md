# Gradle-Midpoint-Plugin
This is a tooling project for midPoint development environments.
The [Gradle](https://gradle.org/) plugin allows interaction with customized [midPoint](https://docs.evolveum.com/midpoint/) projects.

This project aims to fulfill the following goals:
* Aid the setup and maintenance of local development environments.
* Tool-assisted maintenance of midPoint configuration files
* Preparation of VCS controlled midPoint project deployments

# Using this plugin
## Applying this plugin to your Gradle project
1. Apply this plugin to your Gradle build script
```kotlin
// This is Kotlin-Syntax, Groovy would be similar.
plugins {
    id("de.mlessmann.gradle.midpoint-plugin")
}
```
## Types of tasks available
See the task docs at [docs/tasks/index](./docs/tasks/index.md).

# Developing this plugin
## Build setup

## Testing this plugin
The best way of testing this plugin is using a real local midPoint instance because we make sure, the interaction properly works.
That instance should be a clean installation (or at least a minimal image).

To test the plugin is working, we're using Gradle subprojects. For that, there's the [test-projects](./test-projects) directory.
At best, we should test each task on its own so that failures don't influence each other.

Full end-to-end tests and automatic assertions do not exist at the moment because this early in the development, that would be too cumbersome to create and maintain.

## Resources
The following resources may assist in developing features of this plugin.
- [midPoint Studio](https://github.com/Evolveum/midpoint-studio) The official midPoint IDEA plugin that serves examples of how to interact with midPoint instances.
- [midPoint REST docs](https://docs.evolveum.com/midpoint/reference/interfaces/rest/) The official documentation of midPoints REST API.
- [midPoint Samples](https://github.com/Evolveum/midpoint-samples) Example configuration files for midPoint instances.
- [Developing Plugins (Gradle)](https://docs.gradle.org/current/userguide/custom_plugins.html) Resources on how to develop plugins, tasks and conventions.

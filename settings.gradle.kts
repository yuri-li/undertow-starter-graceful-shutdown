pluginManagement {
    repositories {
        mavenLocal()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://repo.spring.io/plugins-snapshot")
        gradlePluginPortal()
        mavenCentral()
    }
}
rootProject.name = "undertow-starter-graceful-shutdown"
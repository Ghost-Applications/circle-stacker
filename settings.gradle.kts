pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.16.2"
}

include(":app")

rootProject.name = "circle-stacker"
rootProject.children.forEach {
    it.buildFileName = "${it.name}.gradle.kts"
}

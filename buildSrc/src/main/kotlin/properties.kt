import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

// namespaces for properties passed to gradle
internal const val projectNamespace = "rocks.ghostapps.circlestacker"
internal const val systemNamespace = "ROCKS_GHOST_APPS_CIRCLESTACKER"

/**
 * Loads properties from gradle.properties, system properties or command line.
 *
 * @see [ProjectProperties](https://docs.gradle.org/current/userguide/build_environment.html#sec:project_properties)
 */
internal fun Project.loadPropertyIntoExtra(
    extraKey: String,
    projectPropertyKey: String,
    systemPropertyKey: String,
    defaultValue: String
) {
    val namespacedProjectProperty = "$projectNamespace.$projectPropertyKey"
    val namespacedSystemProperty = "${systemNamespace}_$systemPropertyKey"

    extra[extraKey] = when {
        hasProperty(namespacedProjectProperty) -> properties[namespacedProjectProperty]
        else -> System.getProperty(namespacedSystemProperty) ?: defaultValue
    }
}

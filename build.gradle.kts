import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import circlestacker.gradle.isNonStable

plugins {
    alias(libs.plugins.version.check)
    alias(libs.plugins.compose.compiler) apply false
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

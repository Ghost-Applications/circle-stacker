plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

val projectNamespace = "rocks.ghostapps.circlestacker"
val systemNamespace = "ROCKS_GHOST_APPS_CIRCLESTACKER"

android {
    namespace = "cash.andrew.circlestacker"
    compileSdk = 37

    val buildNumber = providers.gradleProperty("$projectNamespace.buildNumber")
        .orElse(providers.environmentVariable("$systemNamespace.BUILD_NUMBER"))
        .getOrElse("1")

    defaultConfig {
        applicationId = "rocks.ghostreader.circlestacker"
        minSdk = libs.versions.android.min.sdk.get().toInt()
        targetSdk = libs.versions.android.sdk.get().toInt()
        versionCode = buildNumber.toInt()
        versionName = "Carini"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            val keystoreLocation = providers.gradleProperty("$projectNamespace.keystoreLocation")
                .orElse(providers.environmentVariable("$systemNamespace.KEYSTORE_LOCATION"))
                .getOrElse("debug.keystore")

            val keystorePassword = providers.gradleProperty("$projectNamespace.keystorePassword")
                .orElse(providers.environmentVariable("$systemNamespace.KEYSTORE_PASSWORD"))
                .getOrElse("android")

            val storeKeyAlias = providers.gradleProperty("$projectNamespace.storeKeyAlias")
                .orElse(providers.environmentVariable("$systemNamespace.KEY_ALIAS"))
                .getOrElse("androiddebugkey")

            val aliasKeyPassword = providers.gradleProperty("$projectNamespace.aliasKeyPassword")
                .orElse(providers.environmentVariable("$systemNamespace.KEY_PASSWORD"))
                .getOrElse("android")

            storeFile = file(keystoreLocation)
            storePassword = keystorePassword
            keyAlias = storeKeyAlias
            keyPassword = aliasKeyPassword
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(projects.composeApp)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
}

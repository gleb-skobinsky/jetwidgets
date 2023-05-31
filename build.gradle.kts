plugins {
    id("com.android.application") version "7.4.0"
    id("org.jetbrains.kotlin.android") version "1.8.0"
}

android {
    compileSdk = 33
    namespace = "com.jetwidgets"

    defaultConfig {
        applicationId = "com.jetwidgets"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}

val filamentVersion = "1.8.1"

dependencies {
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.material:material-icons-extended:1.4.3")
    implementation("androidx.compose.ui:ui-tooling:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.appcompat:appcompat:1.6.1")
}

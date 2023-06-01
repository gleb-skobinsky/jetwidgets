plugins {
    id("com.android.library") version "7.4.0"
    id("com.android.application") version "7.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0"
    id("maven-publish")
}

android {
    compileSdk = 33
    namespace = "com.jetwidgets"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    publishing {
        multipleVariants {
            allVariants()
            withJavadocJar()
            withSourcesJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenRelease") {
                groupId = "com.jetwidgets"
                artifactId = "jetwidgets"
                version = "1.0"

                from(components["release"])
            }
            create<MavenPublication>("mavenDebug") {
                groupId = "com.jetwidgets"
                artifactId = "jetwidgets"
                version = "1.0"

                from(components["debug"])
            }
        }
    }
}

plugins {
    id("android.application")
    id("android.application.compose")
    id("org.jetbrains.kotlin.android")
    id("android.hilt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10" apply true
}


android {
    namespace = "github.masterj3y.subscene"
    compileSdk = 33

    defaultConfig {
        applicationId = "github.masterj3y.subscene"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildTypes {

            getByName("release") {
                isMinifyEnabled = true
                isShrinkResources = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "$project.rootDir/tools/proguard-rules.pro"
                )
            }
        }
    }
}

dependencies {

    implementation(project(":core:designsystem"))
    implementation(project(":core:network"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:search-movie"))
    implementation(project(":feature:subtitle"))

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // jetpack
    implementation(libs.androidx.startup)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
}
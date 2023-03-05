plugins {
    id("android.library")
    id("android.hilt")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10" apply true
}

android {

    namespace = "github.masterj3y.data"
}

dependencies {

    api(project(":core:network"))

    implementation(libs.jsoup)

    testImplementation(project(":core:testutils"))
}
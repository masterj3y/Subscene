plugins {
    id("android.library")
    id("android.hilt")
}

android {

    namespace = "github.masterj3y.network"
}

dependencies {

    api(libs.ktor.client.android)
    api(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.logback)

    testImplementation(project(":core:testutils"))
}
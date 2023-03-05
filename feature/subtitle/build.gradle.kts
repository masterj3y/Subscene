plugins {
    id("android.library")
    id("android.library.compose")
    id("android.feature")
    id("android.hilt")
}

android {
    namespace = "github.masterj3y.subtitle"
}

dependencies {

    implementation(project(":library:extensions"))

    implementation(libs.coil.compose)

    testImplementation(project(":core:testutils"))
}
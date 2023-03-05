plugins {
    id("android.library")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10" apply true
}

android {
    namespace = "github.masterj3y.testutils"
}

dependencies {

    api(project(":core:coroutines"))

    api(libs.kotest.assertion.core)

    api(libs.ktor.client.mock)
    api(libs.ktor.client.serialization)

    api(libs.junit)
    api(libs.androidx.test.ext.junit)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.arch.core.testing)
    api(libs.mockito)
}
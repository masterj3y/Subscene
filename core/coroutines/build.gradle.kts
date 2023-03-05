plugins {
    id("android.library")
    id("android.hilt")
}

android {

    namespace = "github.masterj3y.coroutines"
}

dependencies {

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.test)
}
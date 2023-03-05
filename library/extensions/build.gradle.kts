plugins {
    id("android.library")
    id("android.library.compose")
}

android {
    namespace = "github.masterjey.extensions"
}

dependencies {

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.test)

    implementation(libs.androidx.lifecycle.runtimeKtx)
    implementation(libs.androidx.compose.ui)
}
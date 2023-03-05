plugins {
    id("android.library")
    id("android.library.compose")
}

android {

    namespace = "github.masterj3y.navigation"
}

dependencies {

    implementation(libs.androidx.navigation.compose)
}
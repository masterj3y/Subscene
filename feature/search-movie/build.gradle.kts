plugins {
    id("android.library")
    id("android.library.compose")
    id("android.feature")
    id("android.hilt")
}

android {

    namespace = "github.masterj3y.searchmovie"
}

dependencies {

    implementation(project(":library:extensions"))

    implementation(libs.androidx.lifecycle.livedataKtx)

    testImplementation(project(":core:testutils"))
}
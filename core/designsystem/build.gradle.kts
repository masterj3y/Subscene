plugins {
    id("android.library")
    id("android.library.compose")
}

android {

    namespace = "github.masterj3y.designsystem"
}

dependencies {

    api(libs.androidx.appcompat)
    api(libs.androidx.compose.material)

    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
}
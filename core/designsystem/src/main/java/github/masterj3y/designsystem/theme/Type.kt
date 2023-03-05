package github.masterj3y.designsystem.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import github.masterj3y.designsystem.R

private val montserrat = FontFamily(
    Font(resId = R.font.font_montserrat_regular, weight = FontWeight.Normal),
    Font(resId = R.font.font_montserrat_bold, weight = FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(defaultFontFamily = montserrat)
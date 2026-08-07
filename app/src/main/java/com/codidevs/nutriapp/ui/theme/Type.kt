package com.codidevs.nutriapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.R

val Baloo2 = FontFamily(
    Font(R.font.baloo_2_medium, FontWeight.Medium),
    Font(R.font.baloo_2_semibold, FontWeight.SemiBold),
    Font(R.font.baloo_2_bold, FontWeight.Bold),
    Font(R.font.baloo_2_extrabold, FontWeight.ExtraBold)
)

val Nunito = FontFamily(
    Font(R.font.nunito, FontWeight.Normal),
    Font(R.font.nunito_semibold, FontWeight.SemiBold),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_extrabold, FontWeight.ExtraBold)
)

val NutriAppTypography = Typography(
    headlineSmall = TextStyle(fontFamily = Baloo2, fontWeight = FontWeight.Bold, fontSize = 22.sp),
    titleLarge = TextStyle(fontFamily = Baloo2, fontWeight = FontWeight.Bold, fontSize = 18.sp),
    titleMedium = TextStyle(fontFamily = Baloo2, fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
    titleSmall = TextStyle(fontFamily = Baloo2, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
    labelLarge = TextStyle(fontFamily = Baloo2, fontWeight = FontWeight.Bold, fontSize = 14.sp),
    bodyLarge = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    bodyMedium = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
    bodySmall = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.Normal, fontSize = 11.sp)
)
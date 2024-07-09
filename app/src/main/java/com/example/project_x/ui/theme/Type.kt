package com.example.project_x.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.project_x.R

// Set of Material typography styles to start with
val Typography =
  Typography(
    bodyLarge =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
      )
  )

val SFDisplayFont =
  FontFamily(
    Font(R.font.sf_pro_display_regular, FontWeight.Normal),
    Font(R.font.sf_pro_display_medium, FontWeight.Medium),
    Font(R.font.sf_pro_display_bold, FontWeight.Bold),
    Font(R.font.sf_pro_display_semi_bold_italic, FontWeight.SemiBold),
    Font(R.font.sf_pro_display_thin_italic, FontWeight.Thin),
    Font(R.font.sf_pro_display_light_italic, FontWeight.Light),
    Font(R.font.sf_display_black_italic, FontWeight.Black),
    Font(R.font.sf_display_heavy_italic, FontWeight.ExtraBold),
    Font(R.font.sf_pro_display_ultra_light_italic, FontWeight.ExtraLight),
  )

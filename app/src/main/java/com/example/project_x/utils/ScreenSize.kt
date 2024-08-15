package com.example.project_x.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun rememberScreenSize(): ScreenSize {
  val configuration = LocalConfiguration.current
  val screenWidthDp = configuration.screenWidthDp

  return when {
    screenWidthDp < 600 -> ScreenSize.Phone
    screenWidthDp < 840 -> ScreenSize.Foldable
    else -> ScreenSize.Tablet
  }
}

enum class ScreenSize {
  Phone,
  Foldable,
  Tablet,
}

data class SizingValues(
  val imageSize: Dp,
  val buttonHeight: Dp,
  val textSize: TextUnit,
  val titleTextSize: TextUnit,
  val smallTextSize: TextUnit,
)

@Composable
fun provideSizingValues(screenSize: ScreenSize): SizingValues {
  return when (screenSize) {
    ScreenSize.Phone ->
      SizingValues(
        imageSize = 100.dp,
        buttonHeight = 50.dp,
        textSize = 16.sp,
        titleTextSize = 32.sp,
        smallTextSize = 12.sp,
      )
    ScreenSize.Foldable ->
      SizingValues(
        imageSize = 120.dp,
        buttonHeight = 60.dp,
        textSize = 18.sp,
        titleTextSize = 36.sp,
        smallTextSize = 14.sp,
      )
    ScreenSize.Tablet ->
      SizingValues(
        imageSize = 140.dp,
        buttonHeight = 70.dp,
        textSize = 20.sp,
        titleTextSize = 40.sp,
        smallTextSize = 16.sp,
      )
  }
}

package com.example.project_x.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun TestScreen() {
  Column { Text(text = "You Are logged In", modifier = Modifier.fillMaxWidth(), fontSize = 56.sp) }
}

package com.example.project_x.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_x.ui.viewmodel.AuthViewModel

@Composable
fun TestScreen(viewModel: AuthViewModel) {
  val userState by viewModel.userStateHolder.collectAsState()
  Column {
    Text(text = "You Are logged In", modifier = Modifier.fillMaxWidth(), fontSize = 56.sp)
    Button(onClick = { viewModel.logoutUser() }) { Text(text = "Logout") }
  }
}

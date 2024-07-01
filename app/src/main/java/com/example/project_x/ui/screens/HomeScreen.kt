package com.example.project_x.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.project_x.ui.viewmodel.AuthViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: AuthViewModel) {
  val userState = viewModel.loggedInUserStateHolder.collectAsState().value
  if (userState.isLoggedIn) {
    TestScreen()
  } else {
    // Show the login screen
    LoginScreen(modifier = Modifier, viewModel)
  }
}

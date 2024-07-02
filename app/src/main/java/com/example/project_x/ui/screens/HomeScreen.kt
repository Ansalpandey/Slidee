package com.example.project_x.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.project_x.ui.viewmodel.AuthViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: AuthViewModel) {
  val userState by viewModel.userStateHolder.collectAsState()

  // Check if userState is still loading
  if (userState.isLoading) {
    // Show loading indicator or splash screen while determining login state
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      CircularProgressIndicator() // Or any other loading indicator
    }
  } else {
    // User state is not loading, check if logged in
    if (userState.isLoggedIn) {
      TestScreen(viewModel = viewModel)
    } else {
      LoginScreen(viewModel = viewModel)
    }
  }
}

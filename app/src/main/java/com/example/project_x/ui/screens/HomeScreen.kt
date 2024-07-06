package com.example.project_x.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.project_x.ui.viewmodel.AuthViewModel
import com.example.project_x.ui.viewmodel.ProfileViewModel

@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel,
  profileViewModel: ProfileViewModel,
) {
  val userState by authViewModel.userStateHolder.collectAsState()
  val profileState by profileViewModel.userProfileState.collectAsState()

  if (userState.isLoading) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
  } else {
    if (userState.isLoggedIn) {
      LaunchedEffect(key1 = true) {
        profileViewModel.fetchUserProfile()
        Log.d("HomeScreen", "fetchUserProfile: ${profileViewModel.fetchUserProfile()}")
      }
      Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(text = "Welcome ${profileState.data}!", fontSize = 24.sp)
      }
    } else {
      LoginScreen(viewModel = authViewModel)
    }
  }
}

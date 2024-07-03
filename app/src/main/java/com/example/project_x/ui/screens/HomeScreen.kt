package com.example.project_x.ui.screens

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
import com.example.project_x.common.Resource
import com.example.project_x.data.model.User
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
      LaunchedEffect(key1 = true) { profileViewModel.fetchUserProfile() }
      when (profileState) {
        is Resource.Loading -> {
          Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
          }
        }

        is Resource.Success -> {
          val user = (profileState as Resource.Success<User>).data
          Column {
            Text("Welcome ${user?.username}")
            Text("Name: ${user?.name}")
            Text("Email: ${user?.email}")
            Text("Age: ${user?.age}")
            Text("Bio: ${user?.bio}")
            // Add more content here
          }
        }

        is Resource.Error -> {
          Text("Failed to load profile: ${(profileState as Resource.Error).message}")
        }
      }
    } else {
      LoginScreen(viewModel = authViewModel)
    }
  }
}

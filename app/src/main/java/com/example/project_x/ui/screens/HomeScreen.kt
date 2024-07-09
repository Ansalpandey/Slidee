package com.example.project_x.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.project_x.common.Resource
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

  var isProfileFetched by remember { mutableStateOf(false) }

  if (userState.isLoading) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
  } else {
    if (userState.isLoggedIn) {
      LaunchedEffect(key1 = userState.isLoggedIn) {
        if (!isProfileFetched) {
          profileViewModel.fetchUserProfile()
          isProfileFetched = true
        }
      }
      Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        when (val state = profileState) {
          is Resource.Loading -> {
            CircularProgressIndicator()
          }

          is Resource.Success -> {
            Spacer(modifier = Modifier.height(20.dp))
            AsyncImage(
              model = state.data?.user?.profileImage,
              contentDescription = "profile_image",
              modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(120.dp)),
              contentScale = ContentScale.Crop,
            )
            Text(text = "Welcome ${state.data?.user?.name}!", fontSize = 24.sp)
            Text(text = "Email: ${state.data?.user?.email}")
            Text(text = "Courses: ${state.data?.user?.courses}")
            Text(text = "Bio: ${state.data?.user?.bio}")
            Text(text = "Age: ${state.data?.user?.age}")
            Text(text = "Username: ${state.data?.user?.username}")
          }

          is Resource.Error -> {
            Text(text = "Error: ${state.message}")
          }
        }
      }
    } else {
      LoginScreen(viewModel = authViewModel)
    }
  }
}

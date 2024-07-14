package com.example.project_x.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavController
import com.example.project_x.common.Resource
import com.example.project_x.ui.components.CustomAppBar
import com.example.project_x.ui.components.CustomBottomBar
import com.example.project_x.ui.components.PostItem
import com.example.project_x.ui.viewmodel.AuthViewModel
import com.example.project_x.ui.viewmodel.PostViewModel
import com.example.project_x.ui.viewmodel.ProfileViewModel

@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel,
  profileViewModel: ProfileViewModel,
  postViewModel: PostViewModel,
  navController: NavController,
) {
  val userState by authViewModel.userStateHolder.collectAsState()
  val profileState by profileViewModel.userProfileState.collectAsState()
    val posts by postViewModel.posts.collectAsState()
  var isProfileFetched by remember { mutableStateOf(false) }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      if (userState.isLoggedIn) {
        CustomAppBar(
          image = profileState.data?.user?.profileImage,
          name = profileState.data?.user?.name,
        )
      }
    },
    bottomBar = {
      if (userState.isLoggedIn) {
        CustomBottomBar(authViewModel = authViewModel)
      }
    },
  ) { innerPadding ->
    if (userState.isLoading) {
      Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
      }
    } else {
      if (userState.isLoggedIn) {
        LaunchedEffect(key1 = userState.isLoggedIn) {
          if (!isProfileFetched) {
            profileViewModel.fetchUserProfile()
            isProfileFetched = true
              postViewModel.getPosts()
          }
        }
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val state = profileState) {
                is Resource.Loading -> {
                    item { CircularProgressIndicator() }
                }

                is Resource.Success -> {
                    val postData = posts.data
                    if (postData.isNullOrEmpty()) {
                        item { Text(text = "No posts available") }
                    } else {
                        items(postData) { post -> PostItem(post = post) }
                    }
                }

                is Resource.Error -> {
                    item { Text(text = "Error: ${state.message}") }
            }
          }
        }
      } else {
        LoginScreen(authViewModel = authViewModel, navController = navController)
      }
    }
  }
}

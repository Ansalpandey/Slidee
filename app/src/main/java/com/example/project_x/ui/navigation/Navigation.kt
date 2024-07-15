package com.example.project_x.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.project_x.ui.screens.CreatePostScreen
import com.example.project_x.ui.screens.HomeScreen
import com.example.project_x.ui.screens.LoginScreen
import com.example.project_x.ui.screens.RegisterScreen
import com.example.project_x.ui.viewmodel.AuthViewModel
import com.example.project_x.ui.viewmodel.PostViewModel
import com.example.project_x.ui.viewmodel.ProfileViewModel

@Composable
fun NavigationSetup(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel,
  profileViewModel: ProfileViewModel,
  postViewModel: PostViewModel,
  navController: NavHostController,
) {
  val userState by authViewModel.userStateHolder.collectAsState()
  val startDestination = if (userState.isLoggedIn) HomeScreen else LoginScreen

  NavHost(navController = navController, startDestination = startDestination) {
    composable<LoginScreen> {
      LoginScreen(authViewModel = authViewModel, modifier = modifier, navController = navController)
    }

    composable<RegisterScreen> {
      RegisterScreen(
        authViewModel = authViewModel,
        modifier = modifier,
        navController = navController,
      )
    }

    composable<HomeScreen> {
      HomeScreen(
        authViewModel = authViewModel,
        modifier = modifier,
        navController = navController,
        profileViewModel = profileViewModel,
        postViewModel = postViewModel,
      )
    }

    composable<CreatePostScreen> {
      CreatePostScreen(
        postViewModel = postViewModel,
        modifier = modifier,
        navController = navController,
      )
    }
  }
}

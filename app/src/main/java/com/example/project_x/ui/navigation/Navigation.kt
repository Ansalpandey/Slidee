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
import com.example.project_x.ui.screens.ProfileScreen
import com.example.project_x.ui.screens.RegisterScreen
import com.example.project_x.ui.screens.SettingsScreen
import com.example.project_x.ui.screens.UserProfileScreen
import com.example.project_x.ui.viewmodel.AuthViewModel
import com.example.project_x.ui.viewmodel.CourseViewModel
import com.example.project_x.ui.viewmodel.PostViewModel
import com.example.project_x.ui.viewmodel.ProfileViewModel

@Composable
fun NavigationSetup(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel,
  profileViewModel: ProfileViewModel,
  postViewModel: PostViewModel,
  navController: NavHostController,
  courseViewModel: CourseViewModel,
) {
  val userState by authViewModel.userStateHolder.collectAsState()
  val startDestination = if (userState.isLoggedIn) Route.HomeScreen else Route.LoginScreen

  NavHost(navController = navController, startDestination = startDestination) {
    composable<Route.LoginScreen> {
      LoginScreen(authViewModel = authViewModel, modifier = modifier, navController = navController)
    }

    composable<Route.RegisterScreen> {
      RegisterScreen(
        authViewModel = authViewModel,
        modifier = modifier,
        navController = navController,
      )
    }

    composable<Route.HomeScreen> {
      HomeScreen(
        authViewModel = authViewModel,
        modifier = modifier,
        navController = navController,
        profileViewModel = profileViewModel,
        postViewModel = postViewModel,
      )
    }

    composable<Route.CreatePostScreen> {
      CreatePostScreen(
        postViewModel = postViewModel,
        modifier = modifier,
        navController = navController,
      )
    }

    composable<Route.ProfileScreen> {
      ProfileScreen(
        modifier = modifier,
        profileViewModel = profileViewModel,
        navController = navController,
        postViewModel = postViewModel
      )
    }

    composable<Route.UserProfileScreen> { backStackEntry ->
      UserProfileScreen(
        modifier = modifier,
        profileViewModel = profileViewModel,
        userId = backStackEntry.arguments?.getString("userId") ?: "",
        navController = navController,
        postViewModel = postViewModel
      )
    }

    composable<Route.SettingsScreen> {
      SettingsScreen(
        modifier = modifier,
        navController = navController)
    }
  }
}

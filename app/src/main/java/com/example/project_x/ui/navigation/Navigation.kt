package com.example.project_x.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.project_x.ui.screens.ChatScreen
import com.example.project_x.ui.screens.CreatePostScreen
import com.example.project_x.ui.screens.EditProfileScreen
import com.example.project_x.ui.screens.ExploreScreen
import com.example.project_x.ui.screens.FollowersScreen
import com.example.project_x.ui.screens.HomeScreen
import com.example.project_x.ui.screens.ImageScreen
import com.example.project_x.ui.screens.LoginScreen
import com.example.project_x.ui.screens.NotificationScreen
import com.example.project_x.ui.screens.ProfileScreen
import com.example.project_x.ui.screens.RegisterScreen
import com.example.project_x.ui.screens.SettingsScreen
import com.example.project_x.ui.screens.UserProfileScreen
import com.example.project_x.ui.viewmodel.AuthViewModel
import com.example.project_x.ui.viewmodel.CourseViewModel
import com.example.project_x.ui.viewmodel.PostViewModel
import com.example.project_x.ui.viewmodel.ProfileViewModel
import com.example.project_x.ui.viewmodel.SearchViewModel

@Composable
fun NavigationSetup(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel,
  profileViewModel: ProfileViewModel,
  postViewModel: PostViewModel,
  navController: NavHostController,
  courseViewModel: CourseViewModel,
  searchViewModel: SearchViewModel,
) {
  val userState by authViewModel.userStateHolder.collectAsState()
  val startDestination = if (userState.isLoggedIn) Route.HomeScreen else Route.LoginScreen

  NavHost(navController = navController, startDestination = startDestination) {
    composable<Route.LoginScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) {
      LoginScreen(authViewModel = authViewModel, modifier = modifier, navController = navController)
    }

    composable<Route.RegisterScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) {
      RegisterScreen(
        authViewModel = authViewModel,
        modifier = modifier,
        navController = navController,
      )
    }

    composable<Route.HomeScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) {
      HomeScreen(
        authViewModel = authViewModel,
        modifier = modifier,
        navController = navController,
        profileViewModel = profileViewModel,
        postViewModel = postViewModel,
      )
    }

    composable<Route.CreatePostScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) {
      CreatePostScreen(
        postViewModel = postViewModel,
        modifier = modifier,
        navController = navController,
      )
    }

    composable<Route.ProfileScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) {
      ProfileScreen(
        modifier = modifier,
        profileViewModel = profileViewModel,
        navController = navController,
        postViewModel = postViewModel,
      )
    }

    composable<Route.UserProfileScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) { backStackEntry ->
      UserProfileScreen(
        modifier = modifier,
        profileViewModel = profileViewModel,
        userId = backStackEntry.arguments?.getString("userId") ?: "",
        navController = navController,
        postViewModel = postViewModel,
      )
    }

    composable<Route.SettingsScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) {
      SettingsScreen(modifier = modifier, navController = navController)
    }

    composable<Route.EditProfileScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) { backStackEntry ->
      EditProfileScreen(
        modifier = modifier,
        navController = navController,
        profileViewModel = profileViewModel,
        username = backStackEntry.arguments?.getString("username") ?: "",
        name = backStackEntry.arguments?.getString("name") ?: "",
        bio = backStackEntry.arguments?.getString("bio") ?: "",
        profileImage = backStackEntry.arguments?.getString("profileImage") ?: "",
        location = backStackEntry.arguments?.getString("location") ?: "",
        email = backStackEntry.arguments?.getString("email") ?: "",
        age = backStackEntry.arguments?.getString("age") ?: "",
      )
    }

    composable<Route.ChatScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) {
      ChatScreen(modifier = modifier, navController = navController)
    }

    composable<Route.ExploreScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) {
      ExploreScreen(
        modifier = modifier,
        navController = navController,
        searchViewModel = searchViewModel,
        profileViewModel = profileViewModel,
      )
    }

    composable<Route.NotificationScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) {
      NotificationScreen(modifier = modifier, navController = navController)
    }

    composable<Route.ImageScreen>(
      enterTransition = {
        slideIntoContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Right,
          animationSpec = tween(durationMillis = 300),
        )
      },
      exitTransition = {
        slideOutOfContainer(
          towards = AnimatedContentTransitionScope.SlideDirection.Left,
          animationSpec = tween(durationMillis = 300),
        )
      },
    ) {
      val images = it.arguments?.getStringArray("images")?.toList() ?: emptyList()
      val initialPage = it.arguments?.getInt("initialPage") ?: 0

      ImageScreen(images = images, initialPage = initialPage) { navController.popBackStack() }
    }

    composable<Route.FollowersScreen> { backStackEntry ->
      val userId = backStackEntry.arguments?.getString("userId") ?: ""
      val followersCount = backStackEntry.arguments?.getInt("followersCount") ?: 0
      FollowersScreen(
        userId = userId,
        modifier = modifier,
        profileViewModel = profileViewModel,
        searchViewModel = searchViewModel,
        navController = navController,
        followersCount = followersCount,
      )
    }
  }
}

package com.example.project_x.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.project_x.ui.screens.HomeScreen
import com.example.project_x.ui.screens.LoginScreen
import com.example.project_x.ui.screens.RegisterScreen
import com.example.project_x.ui.viewmodel.AuthViewModel
import com.example.project_x.ui.viewmodel.CourseViewModel
import com.example.project_x.ui.viewmodel.ProfileViewModel

@Composable
fun NavigationSetup(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    courseViewModel: CourseViewModel,
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = LoginScreen) {
        composable<LoginScreen> {
            LoginScreen(
                authViewModel = authViewModel,
                modifier = modifier,
                navController = navController
            )
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
                courseViewModel = courseViewModel,
            )
        }
    }
}

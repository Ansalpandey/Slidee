package com.example.project_x

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_x.ui.screens.HomeScreen
import com.example.project_x.ui.theme.ProjectXTheme
import com.example.project_x.ui.viewmodel.AuthViewModel
import com.example.project_x.ui.viewmodel.CourseViewModel
import com.example.project_x.ui.viewmodel.PostViewModel
import com.example.project_x.ui.viewmodel.ProfileViewModel
import com.example.project_x.ui.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private val splashViewModel: SplashViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val splashScreen = installSplashScreen()
    splashScreen.setKeepOnScreenCondition { splashViewModel.isSplashShow.value }
    enableEdgeToEdge()
    setContent {
        val authViewModel: AuthViewModel by viewModels()
        val profileViewModel: ProfileViewModel by viewModels()
        val courseViewModel: CourseViewModel by viewModels()
        val postViewModel: PostViewModel by viewModels()
        val navController = rememberNavController()
      ProjectXTheme {
          Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
              MyApp(
                  modifier = Modifier.fillMaxSize(),
                  authViewModel = authViewModel,
                  profileViewModel = profileViewModel,
                  navController = navController,
                  postViewModel = postViewModel,
              )
          }
      }
    }
  }
}

@Composable
fun MyApp(
  modifier: Modifier,
  authViewModel: AuthViewModel,
  profileViewModel: ProfileViewModel,
  postViewModel: PostViewModel,
  navController: NavController,
) {
    HomeScreen(
        authViewModel = authViewModel,
        profileViewModel = profileViewModel,
        postViewModel = postViewModel,
        modifier = modifier,
        navController = navController,
    )
    //
    //  RegisterScreen(authViewModel = authViewModel, navController = navController)

    //  LoginScreen(authViewModel = authViewModel, navController = navController)
}

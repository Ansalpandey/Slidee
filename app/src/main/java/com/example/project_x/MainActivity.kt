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
import com.example.project_x.ui.screens.ProfileScreen
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
                  courseViewModel = courseViewModel,
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
  courseViewModel: CourseViewModel,
  navController: NavController,
) {
    //    NavigationSetup(
    //        authViewModel = authViewModel,
    //        profileViewModel = profileViewModel,
    //        postViewModel = postViewModel,
    //        navController = navController as NavHostController,
    //    )

    ProfileScreen(
        profileViewModel = profileViewModel,
        postViewModel = postViewModel,
        courseViewModel = courseViewModel,
    )
}

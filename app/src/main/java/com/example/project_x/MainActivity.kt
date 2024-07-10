package com.example.project_x

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.project_x.ui.screens.HomeScreen
import com.example.project_x.ui.theme.ProjectXTheme
import com.example.project_x.ui.viewmodel.AuthViewModel
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
      val authViewModel: AuthViewModel = hiltViewModel()
      val profileViewModel: ProfileViewModel = hiltViewModel()
      ProjectXTheme {
        MyApp(
          modifier = Modifier.fillMaxSize(),
          authViewModel = authViewModel,
          profileViewModel = profileViewModel,
        )
      }
    }
  }
}

@Composable
fun MyApp(modifier: Modifier, authViewModel: AuthViewModel, profileViewModel: ProfileViewModel) {
  HomeScreen(authViewModel = authViewModel, profileViewModel = profileViewModel)
}

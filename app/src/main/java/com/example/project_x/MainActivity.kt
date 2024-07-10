package com.example.project_x

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.project_x.ui.screens.RegisterScreen
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
        ProjectXTheme { MyApp(authViewModel = authViewModel, profileViewModel = profileViewModel) }
    }
  }
}

@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//        HomeScreen(
//            modifier = Modifier.padding(innerPadding),
//            authViewModel = authViewModel,
//            profileViewModel = profileViewModel,
//        )

        RegisterScreen(authViewModel = authViewModel, modifier = Modifier.padding(innerPadding))
  }
}

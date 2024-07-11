package com.example.project_x.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import com.example.project_x.common.Resource
import com.example.project_x.ui.components.CourseItem
import com.example.project_x.ui.components.CustomAppBar
import com.example.project_x.ui.components.CustomBottomBar
import com.example.project_x.ui.viewmodel.AuthViewModel
import com.example.project_x.ui.viewmodel.CourseViewModel
import com.example.project_x.ui.viewmodel.ProfileViewModel

@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel,
  profileViewModel: ProfileViewModel,
  courseViewModel: CourseViewModel,
) {
  val userState by authViewModel.userStateHolder.collectAsState()
  val profileState by profileViewModel.userProfileState.collectAsState()
  val courses by courseViewModel.courses.collectAsState()

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
            courseViewModel.getCourses()
          }
        }
        LazyColumn(
          modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          item {
            when (val state = profileState) {
              is Resource.Loading -> {
                CircularProgressIndicator()
              }

              is Resource.Success -> {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                  items(courses.data?.courses ?: emptyList()) { course ->
                    CourseItem(course = course!!, modifier = modifier
                      .fillMaxWidth()
                      .padding(16.dp))
                  }
                }
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                  items(courses.data?.courses ?: emptyList()) { course ->
                    CourseItem(course = course!!, modifier = modifier
                      .fillMaxWidth()
                      .padding(16.dp))
                  }
                }
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                  items(courses.data?.courses ?: emptyList()) { course ->
                    CourseItem(course = course!!, modifier = modifier
                      .fillMaxWidth()
                      .padding(16.dp))
                  }
                }
              }

              is Resource.Error -> {
                Text(text = "Error: ${state.message}")
              }
            }
          }
        }
      } else {
        LoginScreen(authViewModel = authViewModel)
      }
    }
  }
}

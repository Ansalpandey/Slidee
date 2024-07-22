package com.example.project_x.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project_x.R
import com.example.project_x.common.Resource
import com.example.project_x.ui.components.CourseItem
import com.example.project_x.ui.components.PostItem
import com.example.project_x.ui.viewmodel.ProfileViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(modifier: Modifier = Modifier, profileViewModel: ProfileViewModel) {
  val profileState by profileViewModel.userProfileState.collectAsState()
  val isFollowing by profileViewModel.isFollowing.collectAsState()
  val pagerState = rememberPagerState()
  val coroutineScope = rememberCoroutineScope()
  val tabTitles = listOf("Posts", "Courses")
  var coursesFetched by remember { mutableStateOf(false) }
  var refreshTrigger by remember { mutableStateOf(false) }

  LaunchedEffect(refreshTrigger) {
    profileViewModel.fetchUserProfileById()
    profileViewModel.checkIfFollowing()
  }

  when (val state = profileState) {
    is Resource.Loading -> {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
      }
    }

    is Resource.Success -> {
      Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {
            TopAppBar(
                title = {
                  Row(
                      horizontalArrangement = Arrangement.Center,
                      verticalAlignment = Alignment.CenterVertically,
                  ) {
                    IconButton(onClick = { /*TODO*/ }) {
                      Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "back")
                    }
                    Text(
                        text = "@${state.data?.user?.username ?: ""}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    )
                  }
                },
                actions = {
                  IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "settings")
                  }
                },
            )
          },
      ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
          item {
            Row {
              Column(modifier = Modifier.padding(start = 30.dp)) {
                AsyncImage(
                    model = state.data?.user?.profileImage,
                    contentDescription = "profileImage",
                    modifier = Modifier.clip(CircleShape).size(100.dp),
                    placeholder = painterResource(id = R.drawable.profile),
                    error = painterResource(id = R.drawable.profile),
                    contentScale = ContentScale.Crop,
                )
              }
              Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = state.data?.user?.name ?: "",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                      text = "${state.data?.user?.followersCount ?: 0}",
                      fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                      fontWeight = FontWeight.Light,
                      color = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.padding(end = 5.dp),
                  )
                  Text(
                      text = if (state.data?.user?.followersCount == 1) "Follower" else "Followers",
                      fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                      fontWeight = FontWeight.Light,
                  )
                  Text(
                      text = " • ",
                      fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                      fontWeight = FontWeight.Light,
                  )

                  Text(
                      text = "${state.data?.user?.followingCount ?: 0}",
                      fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                      fontWeight = FontWeight.Light,
                      color = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.padding(end = 5.dp),
                  )
                  Text(
                      text = "Following",
                      fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                      fontWeight = FontWeight.Light,
                  )
                }
              }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column {
              Text(
                  text = state.data?.user?.bio ?: "",
                  modifier = Modifier.fillMaxWidth().padding(start = 30.dp),
              )
              Row(
                  modifier =
                      Modifier.fillMaxWidth().padding(start = 30.dp, top = 10.dp, end = 30.dp),
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                OutlinedIconToggleButton(
                    modifier = Modifier.weight(1f),
                    checked = isFollowing,
                    onCheckedChange = { profileViewModel.toggleFollowUser() }) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isFollowing) {
                          Text(
                              text = "Following",
                              fontWeight = FontWeight.Bold,
                              fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                          )
                        } else {
                          Text(
                              text = "Follow",
                              fontWeight = FontWeight.Bold,
                              fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                          )
                        }
                      }
                    }

                IconButton(onClick = { /*TODO*/ }) {
                  Icon(
                      imageVector = Icons.AutoMirrored.Filled.Send,
                      contentDescription = "message",
                      tint = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.size(32.dp).align(Alignment.CenterVertically),
                  )
                }
              }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column {
              TabRow(
                  selectedTabIndex = pagerState.currentPage,
                  modifier = Modifier.fillMaxWidth(),
              ) {
                tabTitles.forEachIndexed { index, title ->
                  Tab(
                      text = { Text(title) },
                      selected = pagerState.currentPage == index,
                      onClick = {
                        coroutineScope.launch {
                          pagerState.scrollToPage(index)
                          if (index == 1 && !coursesFetched) {
                            coursesFetched = true
                          }
                        }
                      },
                  )
                }
              }

              HorizontalPager(
                  count = tabTitles.size,
                  state = pagerState,
                  modifier = Modifier.fillMaxWidth().height(700.dp) // Set a fixed height
                  ) { page ->
                    when (page) {
                      0 -> {
                        val posts = state.data?.user?.posts
                        if (posts.isNullOrEmpty()) {
                          Icon(
                              painter = painterResource(id = R.drawable.post_stack),
                              contentDescription = "posts_not_found",
                              modifier = Modifier.size(200.dp))
                        } else {
                          LazyColumn(
                              modifier = Modifier.fillMaxSize(),
                              horizontalAlignment = Alignment.CenterHorizontally,
                          ) {
                            items(posts) { post -> PostItem(post = post!!) }
                          }
                        }
                      }
                      1 -> {
                        val courses = state.data?.user?.courses
                        if (courses.isNullOrEmpty()) {
                          Icon(
                              painter = painterResource(id = R.drawable.courses),
                              contentDescription = "courses_not_found",
                              modifier = Modifier.size(200.dp))
                        } else {
                          LazyColumn(
                              modifier = Modifier.fillMaxSize(),
                              horizontalAlignment = Alignment.CenterHorizontally,
                          ) {
                            items(courses) { course ->
                              CourseItem(course = course!!, modifier = Modifier.fillMaxWidth())
                            }
                          }
                        }
                      }
                    }
                  }
            }
          }
        }
      }
    }

    is Resource.Error -> Text(text = "Error: ${state.message}")
  }
}

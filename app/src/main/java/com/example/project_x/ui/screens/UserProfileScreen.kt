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
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project_x.R
import com.example.project_x.common.Resource
import com.example.project_x.ui.components.CourseItem
import com.example.project_x.ui.components.PostItem
import com.example.project_x.ui.navigation.Route
import com.example.project_x.ui.viewmodel.PostViewModel
import com.example.project_x.ui.viewmodel.ProfileViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
  modifier: Modifier = Modifier,
  profileViewModel: ProfileViewModel,
  userId: String,
  postViewModel: PostViewModel,
  navController: NavController,
) {
  val profileState by profileViewModel.userProfileState.collectAsStateWithLifecycle()
  val isFollowing by profileViewModel.isFollowing.collectAsStateWithLifecycle()
  val pagerState = rememberPagerState()
  val coroutineScope = rememberCoroutineScope()
  val tabTitles = listOf("Posts", "Courses")
  var coursesFetched by remember { mutableStateOf(false) }
  val refreshTrigger by remember { mutableStateOf(false) }

  LaunchedEffect(refreshTrigger) { profileViewModel.checkIfFollowing(userId) }

  LaunchedEffect(key1 = userId) { profileViewModel.fetchUserProfileById(userId) }

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
                IconButton(onClick = { navController.popBackStack() }) {
                  Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "back")
                }
                Text(
                  text = "@${state.data?.user?.username ?: ""}",
                  textAlign = TextAlign.Center,
                  modifier = Modifier.fillMaxWidth(),
                  fontSize = MaterialTheme.typography.titleLarge.fontSize,
                )
              }
            }
          )
        },
      ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              AsyncImage(
                model = state.data?.user?.profileImage,
                contentDescription = "profileImage",
                modifier = Modifier.clip(CircleShape).size(80.dp),
                placeholder = painterResource(id = R.drawable.profile),
                error = painterResource(id = R.drawable.profile),
                contentScale = ContentScale.Crop,
              )
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
              ) {
                Text(
                  text = "${state.data?.user?.posts?.size ?: 0}",
                  fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.padding(end = 5.dp),
                )
                Text(
                  text = "Posts",
                  fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                  fontWeight = FontWeight.ExtraBold,
                )
              }
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = "${state.data?.user?.followingCount ?: 0}",
                  fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.padding(end = 5.dp),
                )
                Text(
                  text = "Following",
                  fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                  fontWeight = FontWeight.ExtraBold,
                )
              }
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = "${state.data?.user?.followersCount ?: 0}",
                  fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.padding(end = 5.dp),
                )
                Text(
                  text = "Followers",
                  fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                  fontWeight = FontWeight.ExtraBold,
                )
              }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
              modifier = Modifier.fillMaxWidth().padding(start = 30.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Start,
            ) {
              Text(
                text = (state.data?.user?.name + " "),
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.Bold,
              )
              if (
                profileState.data?.user?.location != null &&
                  profileState.data!!.user?.location != ""
              ) {
                Icon(
                  imageVector = Icons.Default.LocationOn,
                  contentDescription = "location",
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(24.dp),
                )
                Text(
                  text = profileState.data?.user?.location!!,
                  fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                  fontWeight = FontWeight.Bold,
                  color = Color.LightGray,
                  modifier = Modifier.padding(end = 5.dp),
                )
              }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Column(modifier = Modifier.fillMaxWidth().padding(start = 30.dp)) {
              Text(
                text = state.data?.user?.bio ?: "",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
              )
            }
            Row(
              modifier = Modifier.fillMaxWidth().padding(start = 30.dp, top = 10.dp, end = 30.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              OutlinedIconToggleButton(
                modifier = Modifier.weight(1f),
                checked = isFollowing,
                onCheckedChange = { profileViewModel.toggleFollowUser(userId) },
              ) {
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
                modifier = Modifier.fillMaxWidth().height(700.dp), // Set a fixed height
              ) { page ->
                when (page) {
                  0 -> {
                    val posts = state.data?.user?.posts
                    if (posts.isNullOrEmpty()) {
                      Icon(
                        painter = painterResource(id = R.drawable.post_stack),
                        contentDescription = "posts_not_found",
                        modifier = Modifier.size(200.dp),
                      )
                    } else {
                      LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                      ) {
                        items(posts) { post ->
                          PostItem(
                            post = post!!,
                            navController = navController,
                            likePost = { postViewModel.likePost(post._id!!) },
                            unlikePost = { postViewModel.unLikePost(post._id!!) },
                            profileViewModel = profileViewModel,
                            onClick = {
                              navController.navigate(Route.UserProfileScreen(post.createdBy?._id!!))
                            },
                          )
                        }
                      }
                    }
                  }
                  1 -> {
                    val courses = state.data?.user?.courses
                    if (courses.isNullOrEmpty()) {
                      Icon(
                        painter = painterResource(id = R.drawable.courses),
                        contentDescription = "courses_not_found",
                        modifier = Modifier.size(200.dp),
                      )
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

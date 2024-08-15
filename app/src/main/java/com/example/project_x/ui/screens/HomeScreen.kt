package com.example.project_x.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.project_x.R
import com.example.project_x.ui.components.CustomAppBar
import com.example.project_x.ui.components.CustomBottomBar
import com.example.project_x.ui.components.PostItem
import com.example.project_x.ui.navigation.Route
import com.example.project_x.ui.viewmodel.AuthViewModel
import com.example.project_x.ui.viewmodel.PostViewModel
import com.example.project_x.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel,
  profileViewModel: ProfileViewModel,
  postViewModel: PostViewModel,
  navController: NavController,
) {
  val userState by authViewModel.userStateHolder.collectAsStateWithLifecycle()
  val profileState by profileViewModel.loggedInUserProfileState.collectAsStateWithLifecycle()
  val posts = postViewModel.posts.collectAsLazyPagingItems()
  var isProfileFetched by remember { mutableStateOf(false) }
  val lifecycleOwner = LocalLifecycleOwner.current
  val listState = rememberLazyListState()

  // Create scroll behavior
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      if (userState.isLoggedIn) {
        CustomAppBar(
          image = profileState.data?.user?.profileImage,
          name = profileState.data?.user?.name,
          navController = navController,
          scrollBehavior = scrollBehavior, // Pass the scroll behavior
        )
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        shape = RoundedCornerShape(20.dp),
        content = { Icon(imageVector = Icons.Default.Add, contentDescription = "create_post") },
        onClick = {
          navController.navigate(Route.CreatePostScreen)
          navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>("refreshProfile")
            ?.observe(lifecycleOwner) { shouldRefresh ->
              if (shouldRefresh) {
                profileViewModel.refreshProfile()
                postViewModel.getPosts() // Re-fetch posts if needed
              }
            }
        },
      )
    },
    bottomBar = {
      if (userState.isLoggedIn) {
        CustomBottomBar(navController = navController)
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
            postViewModel.getPosts()
            isProfileFetched = true
          }
        }
        LazyColumn(
          state = listState,
          modifier =
            modifier
              .fillMaxSize()
              .padding(innerPadding)
              .nestedScroll(scrollBehavior.nestedScrollConnection), // Attach nested scroll
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          if (posts.itemCount == 0 && posts.loadState.refresh is LoadState.NotLoading) {
            item {
              Box(
                modifier = Modifier.fillParentMaxSize().padding(30.dp),
                contentAlignment = Alignment.Center,
              ) {
                Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier.fillMaxSize(),
                  verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                  Image(
                    painter = painterResource(id = R.drawable.page_not_found),
                    contentDescription = "posts_not_found",
                  )
                  Text(
                    text = "Error 404! Posts not found",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                  )

                  OutlinedButton(onClick = { postViewModel.getPosts() }) {
                    Text(
                      text = "Reload Posts",
                      fontSize = MaterialTheme.typography.labelLarge.fontSize,
                      fontWeight = FontWeight.Medium,
                    )
                  }
                }
              }
            }
          }
          items(posts.itemCount) { index ->
            posts[index]?.let { post ->
              PostItem(
                post = post,
                navController = navController,
                likePost = { postViewModel.likePost(post._id!!) },
                unlikePost = { postViewModel.unLikePost(post._id!!) },
                profileViewModel = profileViewModel,
                postViewModel = postViewModel,
                onClick = {
                  val loggedInUserId = profileState.data?.user?._id
                  if (post.createdBy?._id == loggedInUserId) {
                    navController.navigate(Route.ProfileScreen)
                  } else {
                    navController.navigate(Route.UserProfileScreen(post.createdBy?._id!!))
                  }
                },
              )
            }
          }
          posts.apply {
            when {
              loadState.refresh is LoadState.Loading -> {
                item {
                  Box(
                    modifier = Modifier.fillParentMaxSize(),
                    contentAlignment = Alignment.Center,
                  ) {
                    CircularProgressIndicator()
                  }
                }
              }
              loadState.append is LoadState.Loading -> {
                item {
                  Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                  }
                }
              }
              loadState.append is LoadState.Error -> {
                item {
                  Box(
                    modifier = Modifier.fillParentMaxSize().padding(30.dp),
                    contentAlignment = Alignment.Center,
                  ) {
                    Column(
                      horizontalAlignment = Alignment.CenterHorizontally,
                      modifier = Modifier.fillMaxSize(),
                      verticalArrangement = Arrangement.SpaceEvenly,
                    ) {
                      Image(
                        painter = painterResource(id = R.drawable.page_not_found),
                        contentDescription = "posts_not_found",
                      )
                      Text(
                        text = "Error 404! Posts not found",
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                      )

                      OutlinedButton(onClick = { postViewModel.getPosts() }) {
                        Text(
                          text = "Reload Posts",
                          fontSize = MaterialTheme.typography.labelLarge.fontSize,
                          fontWeight = FontWeight.Medium,
                        )
                      }
                    }
                  }
                }
              }
              loadState.refresh is LoadState.Error -> {
                item {
                  Box(
                    modifier = Modifier.fillParentMaxSize().padding(30.dp),
                    contentAlignment = Alignment.Center,
                  ) {
                    Column(
                      horizontalAlignment = Alignment.CenterHorizontally,
                      modifier = Modifier.fillMaxSize(),
                      verticalArrangement = Arrangement.SpaceEvenly,
                    ) {
                      Image(
                        painter = painterResource(id = R.drawable.page_not_found),
                        contentDescription = "posts_not_found",
                      )
                      Text(
                        text = "Error 404! Posts not found",
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                      )

                      OutlinedButton(onClick = { postViewModel.getPosts() }) {
                        Text(
                          text = "Reload Posts",
                          fontSize = MaterialTheme.typography.labelLarge.fontSize,
                          fontWeight = FontWeight.Medium,
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        }
      } else {
        navController.navigate(Route.LoginScreen)
      }
    }
  }
}

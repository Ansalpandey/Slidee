package com.example.project_x.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project_x.R
import com.example.project_x.data.model.Post
import com.example.project_x.ui.navigation.Route
import com.example.project_x.ui.viewmodel.PostViewModel
import com.example.project_x.ui.viewmodel.ProfileViewModel
import com.example.project_x.utils.getRelativeTimeSpanString
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PostItem(
  modifier: Modifier = Modifier,
  post: Post?,
  navController: NavController,
  onClick: () -> Unit,
  profileViewModel: ProfileViewModel,
  postViewModel: PostViewModel,
  likePost: (String) -> Unit,
  unlikePost: (String) -> Unit,
) {
  val timeAgo = remember { mutableStateOf(getRelativeTimeSpanString(post?.createdAt!!)) }
  val userId = remember {
    profileViewModel.loggedInUserProfileState.value.data?.user?._id.toString()
  }

  // Get the like state from the view model
  val isLiked = remember(post?._id) { mutableStateOf(postViewModel.isPostLiked(post?._id!!)) }
  val likeCount = remember(post?._id) { mutableIntStateOf(post?.likes ?: 0) }

  LaunchedEffect(post?.createdAt) {
    while (true) {
      timeAgo.value = getRelativeTimeSpanString(post?.createdAt!!)
      delay(60000) // Update every minute
    }
  }

  LaunchedEffect(post?._id) {
    isLiked.value = post?.likedBy?.contains(userId) == true
    likeCount.intValue = post?.likes ?: 0
  }

  val pagerState = rememberPagerState()

  Card(
    modifier = Modifier.fillMaxWidth().padding(10.dp),
    shape = RectangleShape,
    colors = CardDefaults.cardColors(Color.Transparent),
  ) {
    Column {
      Row {
        if (post?.createdBy?.profileImage.isNullOrEmpty()) {
          Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "profile_image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(10.dp).clip(CircleShape).size(50.dp),
          )
        } else {
          AsyncImage(
            model = post?.createdBy?.profileImage,
            contentDescription = "profileImage",
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(10.dp).clip(CircleShape).size(50.dp),
          )
        }

        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = post?.createdBy?.name!!,
              fontSize = MaterialTheme.typography.titleLarge.fontSize,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.clickable { onClick.invoke() },
            )
            Text(text = " ${timeAgo.value}", fontSize = 12.sp, fontWeight = FontWeight.Light)
          }
          Text(
            text = "@${post?.createdBy?.username!!}",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold,
          )
          Text(
            text = post.content!!,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Light,
          )

          if (!post.imageUrl.isNullOrEmpty()) {
            Box(modifier = Modifier.fillMaxWidth()) {
              HorizontalPager(
                count = post.imageUrl.size,
                state = pagerState,
                modifier = Modifier.height(300.dp).padding(top = 5.dp, bottom = 10.dp),
              ) { page ->
                Box(
                  modifier =
                    Modifier.padding(horizontal = 4.dp) // Gap between images
                      .fillMaxWidth()
                      .height(300.dp)
                ) {
                  AsyncImage(
                    model = post.imageUrl[page],
                    contentDescription = "post_image",
                    contentScale = ContentScale.Crop,
                    modifier =
                      Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable {
                        navController.navigate(Route.ImageScreen(post.imageUrl, page))
                      },
                  )
                  Box(
                    modifier =
                      Modifier.fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                          brush =
                            Brush.verticalGradient(
                              colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                            )
                        )
                  )
                }
              }
              if (post.imageUrl.size > 1) {
                HorizontalPagerIndicator(
                  pagerState = pagerState,
                  modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                  activeColor = MaterialTheme.colorScheme.primary,
                  inactiveColor = Color.LightGray,
                  indicatorWidth = 6.dp, // Adjust the width for shrinking effect
                  indicatorHeight = 6.dp, // Maintain the height as well
                  spacing = 4.dp, // Adjust spacing
                )
              }
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            IconButton(
              onClick = {
                if (isLiked.value) {
                  unlikePost(post._id!!)
                  postViewModel.unLikePost(post._id)
                  isLiked.value = false
                  likeCount.intValue -= 1
                } else {
                  likePost(post._id!!)
                  postViewModel.likePost(post._id)
                  isLiked.value = true
                  likeCount.intValue += 1
                }
              }
            ) {
              Icon(
                painter =
                  painterResource(
                    id = if (isLiked.value) R.drawable.like_filled else R.drawable.like
                  ),
                modifier = Modifier.size(18.dp),
                contentDescription = "like_btn",
                tint = if (isLiked.value) Color.Red else Color.Gray,
              )
            }
            Text(
              text = "${likeCount.intValue}",
              fontSize = MaterialTheme.typography.titleLarge.fontSize,
              color = Color.Gray,
            )

            IconButton(onClick = { /*TODO*/ }) {
              Icon(
                painter = painterResource(id = R.drawable.comment),
                modifier = Modifier.size(18.dp),
                contentDescription = "comment_btn",
                tint = Color.Gray,
              )
            }
            Text(
              text = "${post.commentsCount}",
              fontSize = MaterialTheme.typography.titleLarge.fontSize,
              color = Color.Gray,
            )

            IconButton(onClick = { /*TODO*/ }) {
              Icon(
                painter = painterResource(id = R.drawable.share),
                modifier = Modifier.size(18.dp),
                contentDescription = "share_btn",
                tint = Color.Gray,
              )
            }

            IconButton(onClick = { /*TODO*/ }) {
              Icon(
                painter = painterResource(id = R.drawable.more),
                modifier = Modifier.size(18.dp),
                contentDescription = "more_btn",
                tint = Color.Gray,
              )
            }
          }
        }
      }
    }
  }
  HorizontalDivider(color = Color.Gray, thickness = 0.25.dp)
}

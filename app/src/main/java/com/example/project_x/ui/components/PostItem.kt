package com.example.project_x.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project_x.R
import com.example.project_x.data.model.Post
import com.example.project_x.ui.viewmodel.ProfileViewModel
import com.example.project_x.utils.getRelativeTimeSpanString
import kotlinx.coroutines.delay

@Composable
fun PostItem(
  modifier: Modifier = Modifier,
  post: Post?,
  navController: NavController,
  onClick: () -> Unit,
  profileViewModel: ProfileViewModel,
  likePost: (String) -> Unit,
  unlikePost: (String) -> Unit,
) {
  val timeAgo = remember { mutableStateOf(getRelativeTimeSpanString(post?.createdAt!!)) }
  val showDialog = remember { mutableStateOf(false) }
  val dialogImageUrl = remember { mutableStateOf<String?>(null) }
  val userId = rememberSaveable { profileViewModel.loggedInUserProfileState.value.data?.user?._id.toString() }
  val isLiked = rememberSaveable(post?._id) { mutableStateOf(post?.likedBy?.contains(userId) == true) }
  val likeCount = rememberSaveable(post?._id) { mutableIntStateOf(post?.likes ?: 0) }

  LaunchedEffect(post?.createdAt) {
    while (true) {
      timeAgo.value = getRelativeTimeSpanString(post?.createdAt!!)
      delay(60000) // Update every minute
    }
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(10.dp),
    shape = RectangleShape,
    colors = CardDefaults.cardColors(Color.Transparent),
  ) {
    Row {
      if (post?.createdBy?.profileImage.isNullOrEmpty()) {
        Image(
          painter = painterResource(id = R.drawable.profile),
          contentDescription = "profile_image",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .padding(10.dp)
            .clip(CircleShape)
            .size(50.dp),
        )
      } else {
        AsyncImage(
          model = post?.createdBy?.profileImage,
          contentDescription = "profileImage",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .padding(10.dp)
            .clip(CircleShape)
            .size(50.dp),
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onClick.invoke() },
          )
          Text(text = " ${timeAgo.value}", fontSize = 12.sp, fontWeight = FontWeight.Light)
        }
        Text(text = "@${post?.createdBy?.username!!}")
        Text(text = post.content!!, fontSize = 14.sp, fontWeight = FontWeight.Light)
        if (!post.imageUrl.isNullOrEmpty()) {
          LazyRow(
            modifier = Modifier
              .fillMaxWidth()
              .height(300.dp)
              .padding(top = 5.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(post.imageUrl) { imageUrl ->
              AsyncImage(
                model = imageUrl,
                contentDescription = "post_image",
                contentScale = ContentScale.Crop,
                modifier =
                Modifier
                  .size(300.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable {
                    dialogImageUrl.value = imageUrl
                    showDialog.value = true
                  },
              )
            }
          }
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = {
              if (isLiked.value) {
                unlikePost(post._id!!)
                isLiked.value = false
                likeCount.intValue -= 1
              } else {
                likePost(post._id!!)
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
              modifier = Modifier.size(24.dp),
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
              modifier = Modifier.size(24.dp),
              contentDescription = "comment_btn",
            )
          }
          Text(
            text = "${post.commentsCount}",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
          )

          IconButton(onClick = { /*TODO*/ }) {
            Icon(
              painter = painterResource(id = R.drawable.share),
              modifier = Modifier.size(24.dp),
              contentDescription = "share_btn",
            )
          }

          IconButton(onClick = { /*TODO*/ }) {
            Icon(
              painter = painterResource(id = R.drawable.more),
              modifier = Modifier.size(24.dp),
              contentDescription = "more_btn",
            )
          }
        }
      }
    }
  }

  // Image Popup Dialog
  if (showDialog.value && dialogImageUrl.value != null) {
    Dialog(onDismissRequest = { showDialog.value = false }) {
      Column { ZoomableImage(imageUrl = dialogImageUrl.value) }
    }
  }
  HorizontalDivider(color = Color.Gray, thickness = 0.25.dp)
}

@Composable
fun ZoomableImage(imageUrl: String?) {
  var scale by remember { mutableFloatStateOf(1f) }
  val animatedScale = remember { Animatable(1f) }

  LaunchedEffect(scale) {
    animatedScale.snapTo(scale) // Update the animated scale
  }

  Box(
    modifier =
    Modifier
      .pointerInput(Unit) { detectTransformGestures { _, _, zoom, _ -> scale *= zoom } }
      .graphicsLayer(
        scaleX = animatedScale.value,
        scaleY = animatedScale.value,
        translationX = 0f, // Keep the image centered
        translationY = 0f, // Keep the image centered
      )
  ) {
    AsyncImage(
      model = imageUrl,
      contentDescription = "popup_post_image",
      contentScale = ContentScale.Inside,
      modifier = Modifier
        .fillMaxWidth()
        .height(400.dp),
    )

    // Reset scale when not interacting
    LaunchedEffect(scale) {
      if (scale != 1f) {
        animatedScale.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 800))
        scale = 1f // Reset scale after animation
      }
    }
  }
}
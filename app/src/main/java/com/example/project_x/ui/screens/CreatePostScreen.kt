package com.example.project_x.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project_x.R
import com.example.project_x.common.Resource
import com.example.project_x.data.model.PostRequest
import com.example.project_x.ui.viewmodel.PostViewModel

@Composable
fun CreatePostScreen(
  modifier: Modifier = Modifier,
  postViewModel: PostViewModel,
  navController: NavController,
) {
  val postState by postViewModel.post.collectAsState()
  val context = LocalContext.current
  var content by remember { mutableStateOf("") }
  var isLoading by remember { mutableStateOf(false) }
  var postImageUri: Uri? by remember { mutableStateOf(null) }
  var postImageBase64: String by remember { mutableStateOf("") }

  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
      postImageUri = uri
      uri?.let {
        context.contentResolver.openInputStream(it)?.use { inputStream ->
          postImageBase64 = inputStream.toBase64()
        }
      }
    }

  Column(modifier = modifier.fillMaxSize()) {
    Spacer(modifier = Modifier.height(8.dp))

    TextField(
      value = content,
      onValueChange = { content = it },
      label = { Text("Content") },
      modifier = Modifier.fillMaxWidth().height(200.dp),
      maxLines = 10,
    )

    Box(
      modifier =
        Modifier.size(100.dp).clip(CircleShape).background(Color.Gray).clickable {
          launcher.launch(
            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
          )
        },
      contentAlignment = Alignment.Center,
    ) {
      if (postImageUri == null) {
        Image(
          painter = painterResource(id = R.drawable.profile),
          contentDescription = "profile_image",
          modifier = Modifier.fillMaxSize(),
        )
      } else {
        AsyncImage(
          model = postImageUri,
          contentDescription = "profile_image",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop,
        )
      }
    }
    Spacer(modifier = Modifier.height(16.dp))

    Button(
      onClick = {
        val postRequest =
          PostRequest(
            content = content,
            imageUrlBase64 = postImageBase64,
            videoUrl = "", // Assuming you’re not handling video uploads here
            imageUrl = ""
          )
        postViewModel.createPost(postRequest)
        isLoading = true
      },
      modifier = Modifier.align(Alignment.End),
    ) {
      Text("Create Post")
    }

    Spacer(modifier = Modifier.height(16.dp))

    when (postState) {
      is Resource.Loading -> {
        if (isLoading) {
          CircularProgressIndicator()
        }
      }

      is Resource.Success -> {
        LaunchedEffect(Unit) {
          Toast.makeText(context, "Post created successfully!", Toast.LENGTH_SHORT).show()
          isLoading = false
          navController.previousBackStackEntry?.savedStateHandle?.set("shouldRefresh", true)
          navController.popBackStack()
        }
      }

      is Resource.Error -> {
        Toast.makeText(
            context,
            "Failed to create post: ${(postState as Resource.Error).message}",
            Toast.LENGTH_SHORT,
          )
          .show()
        isLoading = false
      }

      else -> {
        isLoading = false
      }
    }
  }
}

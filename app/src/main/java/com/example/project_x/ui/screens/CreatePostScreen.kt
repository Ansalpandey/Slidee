package com.example.project_x.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project_x.R
import com.example.project_x.common.Resource
import com.example.project_x.data.model.PostRequest
import com.example.project_x.ui.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
  modifier: Modifier = Modifier,
  postViewModel: PostViewModel,
  navController: NavController,
) {
  val postState by postViewModel.post.collectAsStateWithLifecycle()
  val context = LocalContext.current
  var content by rememberSaveable { mutableStateOf("") }
  var isLoading by remember { mutableStateOf(false) }
  var postImageUris: List<Uri> by rememberSaveable { mutableStateOf(emptyList()) }
  var postImageBase64s: List<String> by rememberSaveable { mutableStateOf(emptyList()) }

  // Launcher for picking images
  val imageLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
      uris.let {
        postImageUris = postImageUris + it
        it.forEach { uri ->
          context.contentResolver.openInputStream(uri)?.use { inputStream ->
            postImageBase64s = postImageBase64s + inputStream.toBase64()
          }
        }
      }
    }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = { Text(text = "Create Post") },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Back")
          }
        },
        actions = {
          Button(
            onClick = {
              val postRequest =
                PostRequest(
                  content = content,
                  imageUrlBase64 = postImageBase64s,
                  videoUrl = "",
                  imageUrl = "",
                )
              postViewModel.createPost(postRequest)
              isLoading = true
              postViewModel.getPosts()
              navController.popBackStack()
            },
            enabled = content.isNotEmpty() || postImageUris.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
          ) {
            Text("Post")
          }
        },
      )
    },
  ) { paddingValues ->
    Column(modifier = Modifier.padding(paddingValues).fillMaxSize().padding(16.dp)) {
      TextField(
        value = content,
        onValueChange = { content = it },
        modifier = Modifier.fillMaxWidth().weight(1f),
        placeholder = { Text("What's happening?") },
        colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.primary),
        textStyle = MaterialTheme.typography.bodyLarge,
      )

      if (postImageUris.isNotEmpty()) {
        LazyRow(
          modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          // Display the selected images
          items(postImageUris) { uri ->
            Box(
              modifier =
                Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)).background(Color.Gray),
              contentAlignment = Alignment.Center,
            ) {
              AsyncImage(
                model = uri,
                contentDescription = "selected_image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
              )

              // Close button
              IconButton(
                onClick = {
                  val indexToRemove = postImageUris.indexOf(uri)
                  postImageUris = postImageUris.toMutableList().apply { removeAt(indexToRemove) }
                  postImageBase64s =
                    postImageBase64s.toMutableList().apply { removeAt(indexToRemove) }
                },
                modifier =
                Modifier.align(Alignment.Center)
                  .size(50.dp)
                  .background(Color.Gray.copy(alpha = 0.3f), CircleShape),
              ) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "remove_image",
                  tint = Color.White,
                  modifier = Modifier.size(20.dp),
                )
              }
            }
          }

          // Show the "Add more" option if images are less than 5
          if (postImageUris.size < 5) {
            item {
              Box(
                modifier =
                  Modifier.size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .clickable {
                      imageLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                      )
                    },
                contentAlignment = Alignment.Center,
              ) {
                Icon(
                  painter =
                    painterResource(
                      id = R.drawable.image
                    ), // Replace with your "Add Image" icon resource
                  contentDescription = "add_more_images",
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(48.dp),
                )
              }
            }
          }
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        IconButton(
          onClick = {
            imageLauncher.launch(
              PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
          }
        ) {
          Icon(
            painter = painterResource(id = R.drawable.image),
            contentDescription = "pick_image",
            tint = MaterialTheme.colorScheme.primary,
          )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (isLoading) {
          CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 2.dp,
          )
        }
      }
    }

    when (postState) {
      is Resource.Loading -> {
        if (isLoading) {
          CircularProgressIndicator()
        }
      }
      is Resource.Success -> {
        LaunchedEffect(postViewModel.isPostCreated) {
          if (postViewModel.isPostCreated) {
            Toast.makeText(context, "Post created successfully", Toast.LENGTH_SHORT).show()

            // Reset post creation state in the ViewModel
            postViewModel.resetPostCreationState()

            // Reset other state variables
            content = ""
            postImageUris = emptyList()
            postImageBase64s = emptyList()
            isLoading = false
          }
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

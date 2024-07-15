package com.example.project_x.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_x.common.Resource
import com.example.project_x.data.model.PostRequest
import com.example.project_x.ui.navigation.HomeScreen
import com.example.project_x.ui.viewmodel.PostViewModel

@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel,
    navController: NavController
) {
    val postState by postViewModel.post.collectAsState()
    val context = LocalContext.current
    var content by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
          .fillMaxWidth()
          .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp),
            maxLines = 10
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val postRequest = PostRequest(content, "", "")
                postViewModel.createPost(postRequest)
                isLoading = true
            },
            modifier = Modifier.align(Alignment.End)
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
                    navController.navigate(HomeScreen) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            }

            is Resource.Error -> {
                Toast.makeText(
                    context,
                    "Failed to create post: ${(postState as Resource.Error).message}",
                    Toast.LENGTH_SHORT
                ).show()
                isLoading = false
            }

            else -> {
                isLoading = false
            }
        }
    }
}

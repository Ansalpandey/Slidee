package com.example.project_x.ui.screens

import android.net.Uri
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project_x.data.model.EditProfileRequest
import com.example.project_x.ui.navigation.Route
import com.example.project_x.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
  modifier: Modifier = Modifier,
  navController: NavController,
  profileViewModel: ProfileViewModel,
  username: String,
  name: String,
  bio: String,
  location: String,
  email: String,
  age: String,
  profileImage: String,
) {
  var newUsername by remember { mutableStateOf(username) }
  var newName by remember { mutableStateOf(name) }
  var newBio by remember { mutableStateOf(bio) }
  val newProfileImage by remember { mutableStateOf(profileImage) }
  var newLocation by remember { mutableStateOf(location) }
  var newEmail by remember { mutableStateOf(email) }
  var newAge by remember { mutableStateOf(age) }

  var isLoading by remember { mutableStateOf(false) }
  val context = LocalContext.current
  val state by profileViewModel.loggedInUserProfileState.collectAsState()

  var profileImageUri: Uri? by remember { mutableStateOf(null) }
  var profileImageBase64: String? by remember { mutableStateOf(null) }

  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
      profileImageUri = uri
      uri?.let {
        context.contentResolver.openInputStream(it)?.use { inputStream ->
          profileImageBase64 = inputStream.toBase64()
        }
      }
    }

  val isSubmitEnabled =
    remember(
      newUsername,
      newName,
      newBio,
      newProfileImage,
      profileImageUri,
      profileImageBase64,
      newLocation,
      newEmail,
      newAge,
    ) {
      newUsername != username ||
        newName != name ||
        newBio != bio ||
        newLocation != location ||
        newEmail != email ||
        newAge != age ||
        newProfileImage != profileImage ||
        profileImageUri != null
    }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    backgroundColor = MaterialTheme.colorScheme.background,
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
              text = "Edit Profile",
              textAlign = TextAlign.Center,
              modifier = Modifier.fillMaxWidth(),
              fontSize = MaterialTheme.typography.titleLarge.fontSize,
            )
          }
        },
        actions = {
          IconButton(onClick = { navController.navigate(Route.SettingsScreen) }) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "settings")
          }
        },
      )
    },
  ) { paddingValues ->
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      Box(
        modifier =
          Modifier.size(100.dp).clip(CircleShape).clickable {
            launcher.launch(
              PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
          },
        contentAlignment = Alignment.Center,
      ) {
        Box(
          modifier =
            Modifier.size(100.dp).clip(CircleShape).background(Color.Gray).clickable {
              launcher.launch(
                PickVisualMediaRequest(
                  mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                )
              )
            },
          contentAlignment = Alignment.Center,
        ) {
          if (profileImageUri == null) {
            AsyncImage(
              model = profileImage,
              contentDescription = "profile_image",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop,
            )
          } else {
            AsyncImage(
              model = profileImageUri,
              contentDescription = "profile_image",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop,
            )
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
      }
      Spacer(modifier = Modifier.height(16.dp))
      TextField(
        value = newUsername,
        onValueChange = { newUsername = it },
        label = { Text("Username") },
        modifier = Modifier.fillMaxWidth().padding(16.dp),
      )

      TextField(
        value = newName,
        onValueChange = { newName = it },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth().padding(16.dp),
      )

      TextField(
        value = newBio,
        onValueChange = { newBio = it },
        label = { Text("Bio") },
        modifier = Modifier.fillMaxWidth().padding(16.dp),
      )

      TextField(
        value = newEmail,
        onValueChange = { newEmail = it },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth().padding(16.dp),
      )

      TextField(
        value = newLocation,
        onValueChange = { newLocation = it },
        label = { Text("Location") },
        modifier = Modifier.fillMaxWidth().padding(16.dp),
      )

      TextField(
        value = newAge,
        onValueChange = { newAge = it },
        label = { Text("Age") },
        modifier = Modifier.fillMaxWidth().padding(16.dp),
      )

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = {
          val id = state.data?.user?._id!!
          val user =
            EditProfileRequest(
              username = newUsername,
              name = newName,
              bio = newBio,
              profileImage = "",
              profileImageBase64 = profileImageBase64,
              age = newAge.toInt(),
              email = newEmail,
              location = newLocation,
            )

          profileViewModel.editProfile(id, user)
          isLoading = true
        },
        enabled = isSubmitEnabled,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(text = "Submit")
      }
    }
  }
}

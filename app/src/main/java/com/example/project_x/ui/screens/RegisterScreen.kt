package com.example.project_x.ui.screens

import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project_x.R
import com.example.project_x.data.model.UserRequest
import com.example.project_x.ui.viewmodel.AuthViewModel
import java.io.InputStream

@Composable
fun RegisterScreen(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
  val userState = authViewModel.userStateHolder.collectAsState().value
  val context = LocalContext.current
  var email by rememberSaveable { mutableStateOf("") }
  var password by rememberSaveable { mutableStateOf("") }
  var age by rememberSaveable { mutableStateOf("") }
  var name by rememberSaveable { mutableStateOf("") }
  var username by rememberSaveable { mutableStateOf("") }
  var bio by rememberSaveable { mutableStateOf("") }
  var profileImageUri: Uri? by remember { mutableStateOf(null) }
  var profileImageBase64: String? by remember { mutableStateOf(null) }

  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
      profileImageUri = uri
      uri?.let {
        context.contentResolver.openInputStream(it)?.use { inputStream ->
          profileImageBase64 = inputStream.toBase64()
          Log.d("RegisterScreen", "Converted image to Base64: $profileImageBase64")
        }
      }
    }

  if (userState.isLoading) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      CircularProgressIndicator()
    }
  } else {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Box(
        modifier = Modifier
          .size(100.dp)
          .clip(CircleShape)
          .background(Color.Gray)
          .clickable {
            launcher.launch(
              PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
          },
        contentAlignment = Alignment.Center,
      ) {
        if (profileImageUri == null) {
          Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "profile_image",
            modifier = Modifier.fillMaxSize(),
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
      OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth(),
      )
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
      )
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
      )
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedTextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Username") },
        modifier = Modifier.fillMaxWidth(),
      )
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedTextField(
        value = bio,
        onValueChange = { bio = it },
        label = { Text("Bio") },
        modifier = Modifier.fillMaxWidth(),
      )
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedTextField(
        value = age,
        onValueChange = { age = it },
        label = { Text("Age") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
      )
      Spacer(modifier = Modifier.height(16.dp))
      Button(
        onClick = {
          val user = UserRequest(
            name = name,
            email = email,
            age = age.toInt(),
            username = username,
            password = password,
            bio = bio,
            profileImageBase64 = profileImageBase64,
          )
          authViewModel.registerUser(user)
          Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Register")
      }
      if (userState.error?.isNotBlank() == true) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = userState.error, color = Color.Red, textAlign = TextAlign.Center)
      }
    }
  }
}

fun InputStream.toBase64(): String {
  val byteArray = this.readBytes()
  return Base64.encodeToString(byteArray, Base64.DEFAULT)
}


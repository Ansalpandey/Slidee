package com.example.project_x.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.project_x.data.model.User
import com.example.project_x.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(modifier: Modifier = Modifier, viewModel: AuthViewModel) {
  val userState = viewModel.userStateHolder.value

  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var age by remember { mutableIntStateOf(0) }
  var name by remember { mutableStateOf("") }
  var username by remember { mutableStateOf("") }
  var bio by remember { mutableStateOf("") }

  if (userState.isLoading) {
    CircularProgressIndicator()
  }

  Column(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center) {

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = age.toString(),
            onValueChange = { age = it.toIntOrNull() ?: 0 },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
              val user = User(name = name, email = email, age = age, username = username, password = password, bio = bio)
              viewModel.registerUser(user)
            },
            modifier = Modifier.fillMaxWidth()) {
              Text("Register")
            }
        if (userState.error.isNotBlank()) {
          Spacer(modifier = Modifier.height(16.dp))
          Text(text = userState.error, color = Color.Red, textAlign = TextAlign.Center)
        }
      }
}

// @Composable
// @Preview(showBackground = true)
// fun RegisterScreenPreview() {
//    RegisterScreen(viewModel = AuthViewModel(UserRepository()))
// }

package com.example.project_x.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.project_x.data.model.UserRequest
import com.example.project_x.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier, viewModel: AuthViewModel) {
  val userState = viewModel.userStateHolder.collectAsState().value
  val context = LocalContext.current
  var emailOrUsername by rememberSaveable { mutableStateOf("") }
  var password by rememberSaveable { mutableStateOf("") }

  if (userState.isLoading) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      CircularProgressIndicator()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    OutlinedTextField(
      value = emailOrUsername,
      onValueChange = { emailOrUsername = it },
      label = { Text("Email or Username") },
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
      value = password,
      onValueChange = { password = it },
      label = { Text("Password") },
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
      onClick = {
        val user =
          UserRequest(
            email = if (emailOrUsername.contains('@')) emailOrUsername else null,
            username = if (!emailOrUsername.contains('@')) emailOrUsername else null,
            password = password,
          )
        viewModel.loginUser(user)
        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
      },
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text("Login")
    }

    if (userState.error?.isNotBlank() == true) {
      Spacer(modifier = Modifier.height(16.dp))
      Text(text = userState.error, color = Color.Red, textAlign = TextAlign.Center)
    }
  }
}

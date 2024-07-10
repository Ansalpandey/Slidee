package com.example.project_x.ui.screens

import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_x.R
import com.example.project_x.data.model.UserRequest
import com.example.project_x.ui.theme.ButtonColor
import com.example.project_x.ui.theme.LeadingIconColor
import com.example.project_x.ui.theme.SFDisplayFont
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
    ) {
      Image(
        painter = painterResource(id = R.drawable.app_icon),
        contentDescription = "app_icon",
        modifier = Modifier.height(200.dp),
      )
      Text(
        text = "Welcome Slidee 👋",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = SFDisplayFont,
      )
      Text(
        text = "Sign Up and enjoy our community",
        fontWeight = FontWeight.Light,
        fontFamily = SFDisplayFont,
        fontSize = 18.sp,
        color = Color.Gray,
      )
      Spacer(modifier = Modifier.height(16.dp))
      OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "name_icon",
            tint = LeadingIconColor,
            modifier = Modifier.size(28.dp),
          )
        },
      )
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
          Icon(
            painter = painterResource(id = R.drawable.email_icon),
            contentDescription = "email_icon",
            tint = LeadingIconColor,
            modifier = Modifier.size(24.dp),
          )
        },
      )
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        leadingIcon = {
          Icon(
            painter = painterResource(id = R.drawable.password_icon),
            contentDescription = "password_icon",
            tint = LeadingIconColor,
            modifier = Modifier.size(32.dp),
          )
        },
      )
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedTextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Username") },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "name_icon",
            tint = LeadingIconColor,
            modifier = Modifier.size(28.dp),
          )
        },
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
      Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text(text = "By continuing you agree to our")
        Text(text = " Terms of Service", fontSize = 18.sp, fontWeight = FontWeight.Bold)
      }
      Row {
        Text(text = "and ")
        Text(text = "Privacy Policy", fontSize = 18.sp, fontWeight = FontWeight.Bold)
      }
      Spacer(modifier = Modifier.height(16.dp))
      Button(
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp),
        colors = ButtonDefaults.buttonColors(ButtonColor),
        onClick = {
          val user =
            UserRequest(
              name = name,
              email = email,
              age = age.toInt(),
              username = username,
              password = password,
              bio = bio,
            )
          authViewModel.registerUser(user)
          Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
        },
      ) {
        Text("Create Account")
      }

      Spacer(modifier = Modifier.height(16.dp))
      Row {
        Text(text = "Already Have an Account?")
        Text(text = "Sign In", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ButtonColor)
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

package com.example.project_x.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_x.R
import com.example.project_x.data.model.UserRequest
import com.example.project_x.ui.theme.ButtonColor
import com.example.project_x.ui.theme.LeadingIconColor
import com.example.project_x.ui.theme.SFDisplayFont
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
    verticalArrangement = Arrangement.Top,
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
      text = "Enter your Email & Password to Sign in",
      fontWeight = FontWeight.Light,
      fontFamily = SFDisplayFont,
      fontSize = 18.sp,
      color = Color.Gray,
    )
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
      value = emailOrUsername,
      onValueChange = { emailOrUsername = it },
      label = { Text("Email or Username", color = Color.LightGray) },
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
      label = { Text("Password", color = Color.LightGray) },
      modifier = Modifier.fillMaxWidth(),
      leadingIcon = {
        Icon(
          painter = painterResource(id = R.drawable.password_icon),
          contentDescription = "password_icon",
          tint = LeadingIconColor,
          modifier = Modifier.size(32.dp),
        )
      },
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
      colors = ButtonDefaults.buttonColors(ButtonColor),
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
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
    ) {
      Text(
        "Sign In",
        fontFamily = SFDisplayFont,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 16.sp,
      )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = "Forgot Password?", color = Color.LightGray)
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
      Spacer(modifier = Modifier.height(20.dp))

      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(modifier = Modifier.weight(1f), thickness = 2.dp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "OR", fontSize = 16.sp)
        Spacer(modifier = Modifier.width(4.dp))
        HorizontalDivider(modifier = Modifier.weight(1f), thickness = 2.dp)
      }
    }
    Spacer(modifier = Modifier.height(20.dp))
    OutlinedButton(
      onClick = { /*TODO*/ },
      shape = RoundedCornerShape(10.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Icon(
          painter = painterResource(id = R.drawable.google_icon),
          contentDescription = "google_icon",
          modifier = Modifier.size(32.dp),
          tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "Sign In with Google",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = SFDisplayFont,
        )
      }
    }
    Spacer(modifier = Modifier.height(20.dp))
    Button(
      onClick = { /*TODO*/ },
      shape = RoundedCornerShape(10.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
      colors = ButtonDefaults.buttonColors(Color.Black),
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Icon(
          painter = painterResource(id = R.drawable.apple),
          contentDescription = "google_icon",
          tint = Color.Unspecified,
          modifier = Modifier.size(32.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "Sign In with Apple",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = SFDisplayFont,
          color = Color.White,
        )
      }
    }
    Spacer(modifier = Modifier.height(30.dp))
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
    ) {
      Text(
        text = "Don't have an account?",
        fontWeight = FontWeight.Light,
        fontFamily = SFDisplayFont,
        fontSize = 18.sp,
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = "Sign Up",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontFamily = SFDisplayFont,
        color = ButtonColor,
      )
    }
  }
  if (userState.error?.isNotBlank() == true) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = userState.error, color = Color.Red, textAlign = TextAlign.Center)
  }
}

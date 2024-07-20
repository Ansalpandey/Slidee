package com.example.project_x.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.project_x.ui.viewmodel.AuthViewModel

@Composable
fun CustomBottomBar(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
  BottomAppBar(modifier = modifier, containerColor = Color.Transparent) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
      }

      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Explore, contentDescription = "Explore")
      }
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
      }
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.AutoMirrored.Filled.Message, contentDescription = "Message")
      }
    }
  }
}

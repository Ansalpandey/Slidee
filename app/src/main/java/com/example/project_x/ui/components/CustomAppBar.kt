package com.example.project_x.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project_x.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(modifier: Modifier = Modifier, image: String?, name: String?) {
  TopAppBar(
    modifier = Modifier.fillMaxWidth(),
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "Welcome, ",
          fontSize = MaterialTheme.typography.titleLarge.fontSize,
          fontWeight = FontWeight.Light
        )
        Text(
          text = "$name 👋",
          fontSize = MaterialTheme.typography.titleLarge.fontSize,
          fontWeight = FontWeight.Bold
        )
      }
    },
    actions = {
      if (image.isNullOrEmpty())
        Image(
          painter = painterResource(id = R.drawable.profile),
          contentDescription = "profile_image",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .padding(10.dp)
            .clip(CircleShape)
            .size(50.dp),
        )
      else {
        AsyncImage(
          model = image,
          contentDescription = "profileImage",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .padding(10.dp)
            .clip(CircleShape)
            .size(50.dp),
        )
      }
    },
  )
}

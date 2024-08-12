package com.example.project_x.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project_x.data.model.Follower

@Composable
fun FollowerItem(modifier: Modifier = Modifier, follower: Follower) {
  Card(
    modifier = Modifier.fillMaxWidth().padding(10.dp),
    shape = RectangleShape,
    colors = CardDefaults.cardColors(Color.Transparent),
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.fillMaxWidth().size(60.dp).padding(start = 10.dp, end = 10.dp),
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
          model = follower.profileImage,
          contentDescription = "profile_image",
          modifier = Modifier.size(50.dp).clip(CircleShape),
        )
        Column {
          Row {
            Text(
              text = follower.name ?: "",
              modifier = Modifier.padding(start = 10.dp),
              fontSize = MaterialTheme.typography.titleLarge.fontSize,
              fontWeight = FontWeight.Bold,
            )
            Text(
              text = "@${follower.username}",
              fontSize = MaterialTheme.typography.titleMedium.fontSize,
              modifier = Modifier.padding(start = 10.dp),
              color = Color.Gray,
              fontWeight = FontWeight.SemiBold,
            )
          }
          Text(
            text = follower.bio ?: "",
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            modifier = Modifier.padding(start = 10.dp),
            color = Color.Gray,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontWeight = FontWeight.SemiBold,
          )
        }
      }
    }
  }
  HorizontalDivider(color = Color.Gray, thickness = 0.25.dp)
}

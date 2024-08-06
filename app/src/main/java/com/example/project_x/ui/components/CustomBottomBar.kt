package com.example.project_x.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.project_x.ui.navigation.Route
import com.example.project_x.ui.viewmodel.AuthViewModel

@Composable
fun CustomBottomBar(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel,
  navController: NavController,
) {
  var selectedItem by remember { mutableIntStateOf(0) }

  NavigationBar {
    bottomNavItems.forEachIndexed { index, bottomNavItem ->
      NavigationBarItem(
        selected = selectedItem == index,
        onClick = {
//          navController.navigate(bottomNavItem.route)
          selectedItem = index
        },
        icon = {
          Icon(
            imageVector =
              if (selectedItem == index) bottomNavItem.selectedIcon
              else bottomNavItem.unselectedIcon,
            contentDescription = "icon",
          )
        },
        label = { Text(text = bottomNavItem.title) },
        alwaysShowLabel = false,
      )
    }
  }
}

val bottomNavItems =
  listOf(
    BottomNavItem(
      title = "Home",
      unselectedIcon = Icons.Outlined.Home,
      selectedIcon = Icons.Filled.Home,
      route = Route.HomeScreen,
    ),
    BottomNavItem(
      title = "Explore",
      unselectedIcon = Icons.Outlined.Explore,
      selectedIcon = Icons.Filled.Explore,
      route = Route.ExploreScreen,
    ),
    BottomNavItem(
      title = "Notification",
      unselectedIcon = Icons.Outlined.Notifications,
      selectedIcon = Icons.Filled.Notifications,
      route = Route.NotificationScreen,
    ),
    BottomNavItem(
      title = "Message",
      unselectedIcon = Icons.AutoMirrored.Outlined.Chat,
      selectedIcon = Icons.Filled.ChatBubble,
      route = Route.ChatScreen,
    ),
  )

data class BottomNavItem(
  val title: String,
  val unselectedIcon: ImageVector,
  val route: Route,
  val selectedIcon: ImageVector,
  val badgeCount: Int? = null,
)

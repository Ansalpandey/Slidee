package com.example.project_x.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.project_x.ui.navigation.Route

@Composable
fun CustomBottomBar(modifier: Modifier = Modifier, navController: NavController) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination

  var selectedItem by remember { mutableIntStateOf(0) }
  val barItems = remember {
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
  }

  NavigationBar() {
    barItems.forEachIndexed { index, barItem ->
      val selected =
        currentDestination?.hierarchy?.any { it.route == barItem.route::class.qualifiedName } ==
          true

      val animatedIconSize by
        animateDpAsState(
          targetValue = if (selected) 24.dp else 24.dp,
          animationSpec = tween(durationMillis = 100),
          label = "",
        )

      val animatedIconColor by
        animateColorAsState(
          targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
          animationSpec = tween(durationMillis = 100),
          label = "",
        )

      NavigationBarItem(
        selected = selected,
        onClick = {
          selectedItem = index
          navController.navigate(barItem.route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
          }
        },
        icon = {
          Icon(
            imageVector = if (selected) barItem.selectedIcon else barItem.unselectedIcon,
            contentDescription = barItem.title,
            modifier = Modifier.size(animatedIconSize),
            tint = animatedIconColor,
          )
        },
        label = {
          Text(
            text = barItem.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = animatedIconColor,
          )
        },
        alwaysShowLabel = false,
      )
    }
  }
}

data class BottomNavItem(
  val title: String,
  val unselectedIcon: ImageVector,
  val route: Route,
  val selectedIcon: ImageVector,
  val badgeCount: Int? = null,
)

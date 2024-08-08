package com.example.project_x.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.project_x.ui.navigation.Route
import com.example.project_x.ui.viewmodel.AuthViewModel

@Composable
fun CustomBottomBar(
  modifier: Modifier = Modifier,
  navController: NavController,
  authViewModel: AuthViewModel,
) {
  val bottomScreens = remember {
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
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination

  NavigationBar(
    modifier =
      modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background) // Ensure background color is set
        .navigationBarsPadding(), // Adjust padding for navigation bars
    contentColor = MaterialTheme.colorScheme.primary,
    containerColor = Color.Transparent, // Transparent to let background color show through
  ) {
    bottomScreens.forEach { screen ->
      val isSelected =
        currentDestination?.hierarchy?.any { it.route == screen.route::class.qualifiedName } == true
      NavigationBarItem(
        selected = isSelected,
        onClick = {
          navController.navigate(screen.route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
          }
        },
        icon = {
          Icon(
            imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
            contentDescription = screen.title,
          )
        },
        label = {
          Text(
            text = screen.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
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

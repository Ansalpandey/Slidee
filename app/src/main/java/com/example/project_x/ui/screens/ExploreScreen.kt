package com.example.project_x.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_x.ui.components.CustomBottomBar
import com.example.project_x.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
  modifier: Modifier = Modifier,
  authViewModel: AuthViewModel,
  navController: NavController,
) {
  var query by remember { mutableStateOf("") }
  var active by remember { mutableStateOf(false) }
  val items = remember { mutableListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5") }
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = { Text(text = "Explore", fontSize = 28.sp, modifier = Modifier.padding(10.dp)) }
      )
    },
    bottomBar = { CustomBottomBar(authViewModel = authViewModel, navController = navController) },
  ) { innerPadding ->
    Column(
      modifier = Modifier.padding(innerPadding),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = query,
        onQueryChange = { query = it },
        onSearch = {
          items.add(query)
          active = false
          query = ""
        },
        active = active,
        onActiveChange = { active = it },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "search") },
        placeholder = { Text(text = "Search") },
        trailingIcon = {
          if (active) {
            Icon(
              modifier =
                Modifier.clickable {
                  if (query.isNotEmpty()) {
                    query = ""
                  } else {
                    active = false
                  }
                },
              imageVector = Icons.Default.Close,
              contentDescription = "close",
            )
          }
        },
      ) {
        items.forEach() { Row { Text(text = it) } }
      }
    }
  }
}

package com.example.project_x.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project_x.R
import com.example.project_x.common.Resource
import com.example.project_x.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier, profileViewModel: ProfileViewModel) {
    val profileState by profileViewModel.userProfileState.collectAsState()

    LaunchedEffect(key1 = true) { profileViewModel.fetchUserProfile() }

    when (val state = profileState) {
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
        }

        is Resource.Success -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBackIosNew,
                                        contentDescription = "back"
                                    )
                                }
                                Text(
                                    text = "@${profileState.data?.user?.username!!}",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = "back")
                            }
                        },
                    )
                },
            ) { paddingValues ->
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    item {
                        Row {
                            Column(modifier = Modifier.padding(start = 30.dp)) {
                                AsyncImage(
                                    model = profileState.data?.user?.profileImage,
                                    contentDescription = "coverImage",
                                    modifier = Modifier
                                      .clip(CircleShape)
                                      .size(100.dp),
                                    placeholder = painterResource(id = R.drawable.profile),
                                    error = painterResource(id = R.drawable.profile),
                                    contentScale = ContentScale.Crop,
                                )
                                Text(text = profileState.data?.user?.bio!!)
                            }
                            Column(modifier = Modifier.padding(start = 20.dp)) {
                                Text(
                                    text = profileState.data?.user?.name!!,
                                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }
            }
        }

        is Resource.Error -> Text(text = "Error: ${state.message}")
    }
}

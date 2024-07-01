package com.example.project_x.ui.stateholder

import com.example.project_x.data.model.User
import kotlinx.coroutines.flow.Flow

data class UserStateHolder(
  val isLoading: Boolean = false,
  val data: Flow<User>? = null,
  val error: String = "",
  val isLoggedIn: Boolean = false,
)

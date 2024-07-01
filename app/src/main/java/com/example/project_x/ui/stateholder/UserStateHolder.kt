package com.example.project_x.ui.stateholder

import com.example.project_x.data.model.User

data class UserStateHolder(
  val isLoading: Boolean = false,
  val data: User? = null,
  val error: String = "",
)
